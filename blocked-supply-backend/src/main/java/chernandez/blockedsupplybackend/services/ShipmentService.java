package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.domain.ShipmentRecord;
import chernandez.blockedsupplybackend.domain.State;
import chernandez.blockedsupplybackend.domain.User;
import chernandez.blockedsupplybackend.domain.dto.ShipmentInput;
import chernandez.blockedsupplybackend.domain.dto.ShipmentOutput;
import chernandez.blockedsupplybackend.domain.dto.TransferInput;
import chernandez.blockedsupplybackend.repositories.ShipmentRecordRepository;
import chernandez.blockedsupplybackend.repositories.UserRepository;
import chernandez.blockedsupplybackend.utils.EncryptionUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ShipmentService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ShipmentRecordRepository shipmentRecordRepository;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final TransferService transferService;

    @Value("${application.broker.address}")
    private String brokerBaseUrl;
    @Value("${application.security.encryption.secret-key}")
    private String encryptionKey;

    public ShipmentService(ShipmentRecordRepository shipmentRecordRepository, AuthService authService, UserRepository userRepository, TransferService transferService) {
        this.shipmentRecordRepository = shipmentRecordRepository;
        this.authService = authService;
        this.userRepository = userRepository;
        this.transferService = transferService;
    }

    public ResponseEntity<?> createShipment(ShipmentInput shipmentInput) throws Exception {
        ResponseEntity<?> validationResult = checkCreateInputs(shipmentInput);
        if (validationResult != null) {
            return validationResult;
        }

        User user = authService.getUserFromJWT();
        if (user.getBlockchainAddress() == null) {
            return new ResponseEntity<>("User does not have a blockchain address", HttpStatus.FORBIDDEN);
        }
        shipmentInput.setFrom(EncryptionUtil.decrypt(encryptionKey, user.getBlockchainAddress()));

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ShipmentInput> request = new HttpEntity<>(shipmentInput, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    brokerBaseUrl + "/api/shipments", request, String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode responseBody = objectMapper.readTree(response.getBody());

                int shipmentId = responseBody.get("id").asInt();
                String currentOwner = responseBody.get("currentOwner").asText();
                String deliveryDateStr = responseBody.get("deliveryDate").asText();

                LocalDateTime deliveryDate = parseDateToLocalDateTime(deliveryDateStr);

                ShipmentRecord shipmentRecord = new ShipmentRecord(
                        (long) shipmentId,
                        currentOwner,
                        deliveryDate,
                        State.CREATED,
                        user.getId()
                );

                shipmentRecordRepository.save(shipmentRecord);

                createFirstTransaction(shipmentId, user.getId(), shipmentInput.getOrigin(), shipmentInput.getFrom());

                return new ResponseEntity<>(shipmentRecord, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Broker responded with error: " + response.getBody(), response.getStatusCode());
            }

        } catch (Exception e) {
            return new ResponseEntity<>("Failed to call broker: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getShipment(int shipmentId) {
        ShipmentRecord record = shipmentRecordRepository.findById((long) shipmentId).orElse(null);
        if (record == null) {
            return new ResponseEntity<>("Shipment record not found", HttpStatus.NOT_FOUND);
        }

        try {
            String url = brokerBaseUrl + "/api/shipments/" + shipmentId;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                return new ResponseEntity<>("Broker error: " + response.getBody(), response.getStatusCode());
            }

            JsonNode body = objectMapper.readTree(response.getBody());

            ShipmentOutput output = new ShipmentOutput(
                    body.get("id").asInt(),
                    record.getSku(),
                    body.get("name").asText(),
                    body.get("description").asText(),
                    body.get("origin").asText(),
                    body.get("destination").asText(),
                    body.get("deliveryDate").asText(),
                    body.get("units").asInt(),
                    body.get("weight").asInt(),
                    chernandez.blockedsupplybackend.domain.State.values()[body.get("currentState").asInt()],
                    body.get("currentOwner").asText()
            );

            String currentOwner = body.get("currentOwner").asText();
            User newOwner = userRepository.findByBlockchainAddress(EncryptionUtil.encrypt(encryptionKey, currentOwner)).orElse(null);
            if (newOwner == null) {
                throw new RuntimeException("New owner not found");
            }
            output.setCurrentOwner(newOwner.getEmail());

            return new ResponseEntity<>(output, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Failed to retrieve shipment: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> getNextShipmentId() {
        try {
            String url = brokerBaseUrl + "/api/shipments/next-id";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            return new ResponseEntity<>(jsonResponse, response.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to retrieve next shipment ID: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<?> checkCreateInputs(ShipmentInput shipmentInput) {
        if (shipmentInput == null) {
            return new ResponseEntity<>("Invalid shipment input", HttpStatus.BAD_REQUEST);
        }

        if (shipmentInput.getProductName() == null || shipmentInput.getProductName().trim().isEmpty()) {
            return new ResponseEntity<>("Product name cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (shipmentInput.getProductName().matches(".*\\d+.*")) {
            return new ResponseEntity<>("Product name cannot contain numbers", HttpStatus.BAD_REQUEST);
        }
        if (shipmentInput.getProductName().length() < 3 || shipmentInput.getProductName().length() > 100) {
            return new ResponseEntity<>("Product name must contain a minimum of 3 and a maximum of 100 characters", HttpStatus.BAD_REQUEST);
        }

        if (shipmentInput.getDescription() == null || shipmentInput.getDescription().trim().isEmpty()) {
            return new ResponseEntity<>("Description cannot be null", HttpStatus.BAD_REQUEST);
        }
        if (shipmentInput.getDescription().length() > 500) {
            return new ResponseEntity<>("Description cannot exceed 500 characters long", HttpStatus.BAD_REQUEST);
        }

        if (shipmentInput.getOrigin() == null || shipmentInput.getOrigin().trim().isEmpty() ||
                shipmentInput.getDestination() == null || shipmentInput.getDestination().trim().isEmpty()) {
            return new ResponseEntity<>("Origin and destination cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (shipmentInput.getOrigin().equals(shipmentInput.getDestination())) {
            return new ResponseEntity<>("Origin must be different to destination", HttpStatus.BAD_REQUEST);
        }

        if (shipmentInput.getDeliveryDate() == null || shipmentInput.getDeliveryDate().trim().isEmpty()) {
            return new ResponseEntity<>("Delivery date cannot be empty", HttpStatus.BAD_REQUEST);
        }
        try {
            LocalDateTime deliveryDate = parseDateToLocalDateTime(shipmentInput.getDeliveryDate());
            if (deliveryDate.isBefore(LocalDateTime.now())) {
                return new ResponseEntity<>("Delivery date must be future", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Date format invalid, it must be as yyyy-MM-dd", HttpStatus.BAD_REQUEST);
        }

        if (shipmentInput.getUnits() <= 0) {
            return new ResponseEntity<>("Units must be greater than 0", HttpStatus.BAD_REQUEST);
        }
        if (shipmentInput.getWeight() <= 0) {
            return new ResponseEntity<>("Weight must be greater than 0", HttpStatus.BAD_REQUEST);
        }
        if (shipmentInput.getWeight() > 10000) {
            return new ResponseEntity<>("The weight exceeds the maximum available", HttpStatus.BAD_REQUEST);
        }

        return null;
    }

    private void createFirstTransaction(int shipmentId, long currentOwnerId, String origin, String from) throws Exception {
        TransferInput transferInput = new TransferInput();
        transferInput.setShipmentId(shipmentId);

        User newOwner = userRepository.findById(currentOwnerId).orElse(null);
        if (newOwner == null) {
            throw new RuntimeException("New owner not found");
        }
        transferInput.setNewShipmentOwner(newOwner.getEmail());

        transferInput.setNewState(0); //CREATED
        transferInput.setLocation(origin);
        transferInput.setTransferNotes("Shipment created");
        transferInput.setFrom(from);
        transferService.transferShipment(transferInput);
    }

    private LocalDateTime parseDateToLocalDateTime(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateStr, formatter);
        return date.atTime(23, 59);
    }

}