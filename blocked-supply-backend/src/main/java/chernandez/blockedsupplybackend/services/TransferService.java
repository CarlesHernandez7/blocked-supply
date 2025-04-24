package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.domain.Notification;
import chernandez.blockedsupplybackend.domain.ShipmentRecord;
import chernandez.blockedsupplybackend.domain.State;
import chernandez.blockedsupplybackend.domain.User;
import chernandez.blockedsupplybackend.domain.dto.TransferInput;
import chernandez.blockedsupplybackend.domain.dto.TransferOutput;
import chernandez.blockedsupplybackend.repositories.NotificationRepository;
import chernandez.blockedsupplybackend.repositories.ShipmentRecordRepository;
import chernandez.blockedsupplybackend.repositories.UserRepository;
import chernandez.blockedsupplybackend.utils.EncryptionUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TransferService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ShipmentRecordRepository shipmentRecordRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final AuthService authService;

    @Value("${application.broker.address}")
    private String brokerBaseUrl;
    @Value("${application.security.encryption.secret-key}")
    private String encryptionKey;

    public TransferService(ShipmentRecordRepository shipmentRecordRepository, UserRepository userRepository, NotificationRepository notificationRepository, AuthService authService) {
        this.shipmentRecordRepository = shipmentRecordRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.authService = authService;
    }

    public ResponseEntity<?> transferShipment(TransferInput transferInput) throws Exception {
        ResponseEntity<?> validationResult = checkTransferInputs(transferInput);
        if (validationResult != null) {
            return validationResult;
        }

        User user = authService.getUserFromJWT();
        if (user.getBlockchainAddress() == null) {
            return new ResponseEntity<>("User does not have a blockchain address", HttpStatus.FORBIDDEN);
        }
        transferInput.setFrom(EncryptionUtil.decrypt(encryptionKey, user.getBlockchainAddress()));

        ResponseEntity<?> validationResponse = validateAndSetNewOwner(transferInput);
        if (validationResponse.getStatusCode() != HttpStatus.OK) {
            return validationResponse;
        }

        User newOwner = (User) validationResponse.getBody();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<TransferInput> request = new HttpEntity<>(transferInput, headers);

            String url = brokerBaseUrl + "/api/shipments/" + transferInput.getShipmentId() + "/transfer";
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {

                JsonNode responseBody = objectMapper.readTree(response.getBody());
                int shipmentId = responseBody.get("shipmentId").asInt();
                int newState = responseBody.get("newState").asInt();

                ShipmentRecord shipmentRecord = shipmentRecordRepository.findById((long) shipmentId).orElse(null);
                if (shipmentRecord == null) {
                    return new ResponseEntity<>("Shipment record not found", HttpStatus.NOT_FOUND);
                }

                State newStateEnum = State.fromInt(newState);
                shipmentRecord.setState(newStateEnum);
                if (newStateEnum == State.DELIVERED) {
                    shipmentRecord.setDeliveredAt(LocalDateTime.now());
                }

                shipmentRecord.setOwnerId(newOwner.getId());
                shipmentRecord.setOwnerAddress(newOwner.getBlockchainAddress());
                shipmentRecord.addParticipant(newOwner.getId());
                shipmentRecordRepository.save(shipmentRecord);

                //if the new owner is different from the current owner, send a notification
                if (!user.getId().equals(newOwner.getId())) {
                    sendNotification(user.getEmail(), newOwner.getId(), newState, transferInput.getTransferNotes());
                }

                return new ResponseEntity<>(responseBody, response.getStatusCode());

            } else {
                return new ResponseEntity<>("Failed to transfer shipment: " + response.getBody(), response.getStatusCode());
            }

        } catch (Exception e) {
            return new ResponseEntity<>("Failed to transfer shipment: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getTransferHistory(String sku) {
        ShipmentRecord record = shipmentRecordRepository.findBySku(sku).orElse(null);
        if (record == null) {
            return new ResponseEntity<>("Shipment not found", HttpStatus.NOT_FOUND);
        }

        User user = authService.getUserFromJWT();
        if (!record.getParticipants().contains(user.getId())) {
            return new ResponseEntity<>("User is not a participant of this shipment", HttpStatus.FORBIDDEN);
        }

        try {
            String url = brokerBaseUrl + "/api/shipments/" + record.getShipmentId() + "/transfers";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ObjectMapper mapper = new ObjectMapper();
                List<Map<String, Object>> transferList = mapper.readValue(response.getBody(), new TypeReference<>() {
                });
                List<TransferOutput> transfers = new ArrayList<>();

                for (Map<String, Object> transfer : transferList) {
                    String blockchainAddress = EncryptionUtil.encrypt(encryptionKey, transfer.get("newShipmentOwner").toString());
                    User newShipmentOwner = userRepository.findByBlockchainAddress(blockchainAddress).orElse(null);
                    String email = (newShipmentOwner != null) ? newShipmentOwner.getEmail() : "Unknown";

                    TransferOutput t = new TransferOutput(
                            Integer.parseInt(transfer.get("id").toString()),
                            Integer.parseInt(transfer.get("shipmentId").toString()),
                            Integer.parseInt(transfer.get("timestamp").toString()),
                            State.fromBigInt(new BigInteger(transfer.get("newState").toString())),
                            transfer.get("location").toString(),
                            email,
                            transfer.get("transferNotes").toString()
                    );
                    transfers.add(t);
                }

                return new ResponseEntity<>(transfers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to retrieve transfer history", response.getStatusCode());
            }

        } catch (Exception e) {
            return new ResponseEntity<>("Error while retrieving transfer history: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getNextTransferId() {
        try {
            String url = brokerBaseUrl + "/api/transfers/next-id";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            return new ResponseEntity<>(jsonResponse, response.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to retrieve next transfer ID: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<?> checkTransferInputs(TransferInput transferInput) {
        if (transferInput == null) {
            return new ResponseEntity<>("Invalid transfer input", HttpStatus.BAD_REQUEST);
        }
        ShipmentRecord record = shipmentRecordRepository.findById((long) transferInput.getShipmentId()).orElse(null);
        if (record == null) {
            return new ResponseEntity<>("Shipment not found", HttpStatus.NOT_FOUND);
        }
        if (transferInput.getShipmentId() <= 0) {
            return new ResponseEntity<>("Invalid shipment ID", HttpStatus.BAD_REQUEST);
        }
        if (transferInput.getNewShipmentOwner() == null || transferInput.getNewShipmentOwner().trim().isEmpty()) {
            return new ResponseEntity<>("New shipment owner cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (transferInput.getNewState() < 0 || transferInput.getNewState() > 3) {
            return new ResponseEntity<>("Invalid new state", HttpStatus.BAD_REQUEST);
        }
        if (transferInput.getLocation() == null || transferInput.getLocation().trim().isEmpty()) {
            return new ResponseEntity<>("Location cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (transferInput.getLocation().length() < 3 || transferInput.getLocation().length() > 100) {
            return new ResponseEntity<>("Location must contain a minimum of 3 and a maximum of 100 characters", HttpStatus.BAD_REQUEST);
        }
        if (transferInput.getTransferNotes().length() > 100) {
            return new ResponseEntity<>("Transfer notes must contain a maximum of 100 characters", HttpStatus.BAD_REQUEST);
        }

        return null;
    }

    private ResponseEntity<?> validateAndSetNewOwner(TransferInput transferInput) throws Exception {
        try {
            String newOwnerMail = transferInput.getNewShipmentOwner();
            User newOwner = userRepository.findByEmail(newOwnerMail).orElse(null);

            if (newOwner == null) {
                return new ResponseEntity<>("New owner not found", HttpStatus.NOT_FOUND);
            } else if (newOwner.getBlockchainAddress() == null) {
                return new ResponseEntity<>("New owner does not have a blockchain address", HttpStatus.FORBIDDEN);
            }

            transferInput.setNewShipmentOwner(EncryptionUtil.decrypt(encryptionKey, newOwner.getBlockchainAddress()));
            return new ResponseEntity<>(newOwner, HttpStatus.OK);

        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid new shipment owner", HttpStatus.BAD_REQUEST);
        }
    }

    private void sendNotification(String from, Long toUserId, int state, String notes) {
        Notification notification = new Notification();
        notification.setToUserId(toUserId);
        String stateString = State.fromInt(state).toString();
        notification.setMessage("A user with email " + from + " transferred a shipment to you. State: " + stateString + ". Notes: " + notes);
        notificationRepository.save(notification);
    }
}