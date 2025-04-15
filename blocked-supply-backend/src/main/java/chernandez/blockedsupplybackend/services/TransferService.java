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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TransferService {

    @Value("${application.broker.address}")
    private String brokerBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ShipmentRecordRepository shipmentRecordRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final AuthService authService;

    public TransferService(ShipmentRecordRepository shipmentRecordRepository, UserRepository userRepository, NotificationRepository notificationRepository, AuthService authService, AuthService authService1) {
        this.shipmentRecordRepository = shipmentRecordRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.authService = authService1;
    }

    public ResponseEntity<?> transferShipment(TransferInput transferInput) {
        User user = authService.getUserFromJWT();

        if (transferInput == null) {
            return new ResponseEntity<>("Invalid transfer input", HttpStatus.BAD_REQUEST);
        }

        if (user.getBlockchainAddress() == null) {
            return new ResponseEntity<>("User does not have a blockchain address", HttpStatus.FORBIDDEN);
        }

        transferInput.setFrom(user.getBlockchainAddress());

        User newOwner;
        try {
            long newShipmentOwnerId = Long.parseLong(transferInput.getNewShipmentOwner());
            newOwner = userRepository.findById(newShipmentOwnerId).orElse(null);

            if (newOwner == null) {
                return new ResponseEntity<>("New owner not found", HttpStatus.NOT_FOUND);
            } else if (newOwner.getBlockchainAddress() == null) {
                return new ResponseEntity<>("New owner does not have a blockchain address", HttpStatus.FORBIDDEN);
            }

            transferInput.setNewShipmentOwner(newOwner.getBlockchainAddress());

        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid new shipment owner", HttpStatus.BAD_REQUEST);
        }

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

                ShipmentRecord shipmentRecord = shipmentRecordRepository.findById((long)shipmentId).orElse(null);
                if (shipmentRecord == null) {
                    return new ResponseEntity<>("Shipment record not found", HttpStatus.NOT_FOUND);
                }

                shipmentRecord.setState(State.fromInt(newState));
                shipmentRecord.setOwnerId(newOwner.getId());
                shipmentRecord.setOwnerAddress(newOwner.getBlockchainAddress());
                shipmentRecord.addParticipant(newOwner.getId());
                shipmentRecordRepository.save(shipmentRecord);

                sendNotification(newOwner.getId(), newState, transferInput.getTransferNotes());

                return new ResponseEntity<>(responseBody, response.getStatusCode());

            } else {
                return new ResponseEntity<>("Failed to transfer shipment: " + response.getBody(), response.getStatusCode());
            }

        } catch (Exception e) {
            return new ResponseEntity<>("Failed to transfer shipment: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getTransferHistory(int shipmentId) {
        try {
            String url = brokerBaseUrl + "/api/shipments/" + shipmentId + "/transfers";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ObjectMapper mapper = new ObjectMapper();
                List<Map<String, Object>> transferList = mapper.readValue(response.getBody(), new TypeReference<>() {});
                List<TransferOutput> transfers = new ArrayList<>();

                for (Map<String, Object> transfer : transferList) {
                    TransferOutput t = new TransferOutput(
                            Integer.parseInt(transfer.get("id").toString()),
                            Integer.parseInt(transfer.get("shipmentId").toString()),
                            Integer.parseInt(transfer.get("timestamp").toString()),
                            State.fromBigInt(new BigInteger(transfer.get("newState").toString())),
                            transfer.get("location").toString(),
                            transfer.get("newShipmentOwner").toString(),
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

    private void sendNotification(Long toUserId, int state, String notes) {
        Notification notification = new Notification();
        notification.setToUserId(toUserId);
        String stateString = State.fromInt(state).toString();
        notification.setMessage("A user transferred a shipment to you. State: " + stateString + ". Notes: " + notes);
        notificationRepository.save(notification);
    }
}