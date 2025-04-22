package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.domain.ShipmentRecord;
import chernandez.blockedsupplybackend.domain.State;
import chernandez.blockedsupplybackend.domain.User;
import chernandez.blockedsupplybackend.repositories.ShipmentRecordRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShipmentRecordService {

    private final ShipmentRecordRepository shipmentRecordRepository;
    private final AuthService authService;

    public ShipmentRecordService(ShipmentRecordRepository shipmentRecordRepository, AuthService authService) {
        this.shipmentRecordRepository = shipmentRecordRepository;
        this.authService = authService;
    }

    public ResponseEntity<?> getShipmentRecord(int shipmentId) {
        Optional<ShipmentRecord> record = shipmentRecordRepository.findById((long) shipmentId);
        if (record.isEmpty()) {
            return new ResponseEntity<>("Shipment record not found.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(record.get(), HttpStatus.OK);
    }

    public ResponseEntity<Map<String, Object>> getShipmentStatistics() {
        Map<String, Object> response = new HashMap<>();

        long totalShipments = shipmentRecordRepository.count();
        response.put("totalShipments", totalShipments);

        List<ShipmentRecord> activeShipments = shipmentRecordRepository.findByStateNot(State.DELIVERED);
        response.put("activeShipments", (long) activeShipments.size());

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        List<ShipmentRecord> deliveredToday = shipmentRecordRepository.findByStateAndCreatedAtBetween(State.DELIVERED, startOfDay, endOfDay);
        response.put("deliveredToday", (long) deliveredToday.size());
        response.put("successRate", calculateSuccess() + " %");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> getShipmentRecordsByParticipant() {
        User user = authService.getUserFromJWT();
        List<ShipmentRecord> list = shipmentRecordRepository.findByParticipantsContaining(user.getId());
        if (list.isEmpty()) {
            return new ResponseEntity<>("No shipments found for user.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    public ResponseEntity<?> getShipmentRecordsByOwner() {
        User user = authService.getUserFromJWT();
        List<ShipmentRecord> list = shipmentRecordRepository.findByOwnerId(user.getId());
        if (list.isEmpty()) {
            return new ResponseEntity<>("No shipments found for user.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    private int calculateSuccess() {
        List<ShipmentRecord> shipments = shipmentRecordRepository.findAll();
        int totalDelivered = 0;
        int deliveredOnTime = 0;

        for (ShipmentRecord shipment : shipments) {
            if (shipment.getState() == State.DELIVERED && shipment.getDeliveredAt() != null) {
                totalDelivered++;

                if (!shipment.getDeliveredAt().isAfter(shipment.getDeliveryDate())) {
                    deliveredOnTime++;
                }
            }
        }

        if (totalDelivered == 0) return 0;

        return (int) ((deliveredOnTime * 100.0) / totalDelivered);
    }
}
