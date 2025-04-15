package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.domain.ShipmentRecord;
import chernandez.blockedsupplybackend.domain.State;
import chernandez.blockedsupplybackend.domain.User;
import chernandez.blockedsupplybackend.domain.dto.ShipmentInput;
import chernandez.blockedsupplybackend.domain.dto.ShipmentOutput;
import chernandez.blockedsupplybackend.repositories.ShipmentRecordRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ShipmentService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ShipmentRecordRepository shipmentRecordRepository;
    private final AuthService authService;
    @Value("${application.broker.address}")
    private String brokerBaseUrl;

    public ShipmentService(ShipmentRecordRepository shipmentRecordRepository, AuthService authService) {
        this.shipmentRecordRepository = shipmentRecordRepository;
        this.authService = authService;
    }

    public ResponseEntity<?> createShipment(ShipmentInput shipmentInput) {
        User user = authService.getUserFromJWT();

        if (shipmentInput == null || shipmentInput.getWeight() <= 0 || shipmentInput.getUnits() <= 0) {
            return new ResponseEntity<>("Invalid shipment input", HttpStatus.BAD_REQUEST);
        }

        if (user.getBlockchainAddress() == null) {
            return new ResponseEntity<>("User does not have a blockchain address", HttpStatus.FORBIDDEN);
        }

        shipmentInput.setFrom(user.getBlockchainAddress());

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
                String deliveryDate = responseBody.get("deliveryDate").asText();

                ShipmentRecord shipmentRecord = new ShipmentRecord(
                        (long) shipmentId,
                        currentOwner,
                        deliveryDate,
                        State.CREATED,
                        user.getId()
                );

                shipmentRecordRepository.save(shipmentRecord);

                return new ResponseEntity<>(shipmentRecord, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Broker responded with error: " + response.getBody(), response.getStatusCode());
            }

        } catch (Exception e) {
            return new ResponseEntity<>("Failed to call broker: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getShipment(int shipmentId) {
        try {
            String url = brokerBaseUrl + "/api/shipments/" + shipmentId;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                return new ResponseEntity<>("Broker error: " + response.getBody(), response.getStatusCode());
            }

            JsonNode body = objectMapper.readTree(response.getBody());

            ShipmentOutput output = new ShipmentOutput(
                    body.get("id").asInt(),
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

}