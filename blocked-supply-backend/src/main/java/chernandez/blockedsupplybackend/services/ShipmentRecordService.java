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
import java.util.List;
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

    public ResponseEntity<?> getAllShipmentRecords() {
        return new ResponseEntity<>(shipmentRecordRepository.count(), HttpStatus.OK);
    }

    public ResponseEntity<?> getShipmentRecordsInProgress() {
        List<ShipmentRecord> list = shipmentRecordRepository.findByStatusNot(State.DELIVERED);
        if (list.isEmpty()) {
            return new ResponseEntity<>("No shipments in progress.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    public ResponseEntity<?> getShipmentRecordsDeliveredToday() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        List<ShipmentRecord> list = shipmentRecordRepository.findByStatusAndCreatedAtBetween(State.DELIVERED, startOfDay, endOfDay);
        if (list.isEmpty()) {
            return new ResponseEntity<>("No shipments completed today yet.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    public ResponseEntity<?> getShipmentRecordsByParticipant() {
        User user = authService.getUserFromJWT();
        List<ShipmentRecord> list = shipmentRecordRepository.findByParticipantsContaining(user.getId());
        if (list.isEmpty()) {
            return new ResponseEntity<>("No shipments found for user.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    public ResponseEntity<?> getShipmentRecordsByowner() {
        User user = authService.getUserFromJWT();
        List<ShipmentRecord> list = shipmentRecordRepository.findByOwnerId(user.getId());
        if (list.isEmpty()) {
            return new ResponseEntity<>("No shipments found for user.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
