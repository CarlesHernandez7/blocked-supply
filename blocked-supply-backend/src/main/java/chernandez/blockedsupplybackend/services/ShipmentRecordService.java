package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.domain.ShipmentRecord;
import chernandez.blockedsupplybackend.domain.State;
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

    public ShipmentRecordService(ShipmentRecordRepository shipmentRecordRepository) {
        this.shipmentRecordRepository = shipmentRecordRepository;
    }

    public ResponseEntity<?> getShipmentRecord(int shipmentId) {
        Optional<ShipmentRecord> record = shipmentRecordRepository.findById((long) shipmentId);
        if (record.isEmpty()) {
            return new ResponseEntity<>("Shipment record not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(record.get(), HttpStatus.OK);
    }

    public ResponseEntity<?> getAllShipmentRecords() {
        return new ResponseEntity<>(shipmentRecordRepository.count(), HttpStatus.OK);
    }

    public ResponseEntity<?> getShipmentRecordsInProgress() {
        List<ShipmentRecord> list = shipmentRecordRepository.findByStatusNot(State.DELIVERED);
        if (list.isEmpty()) {
            return new ResponseEntity<>("No shipments in progress.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    public ResponseEntity<?> getShipmentRecordsDeliveredToday() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        List<ShipmentRecord> list = shipmentRecordRepository.findByStatusAndCreatedAtBetween(State.DELIVERED, startOfDay, endOfDay);
        if (list.isEmpty()) {
            return new ResponseEntity<>("No shipments completed today yet.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    public ResponseEntity<?> getShipmentRecordsByParticipant(int userId) {
        List<ShipmentRecord> list = shipmentRecordRepository.findByParticipantsContaining((long) userId);
        if (list.isEmpty()) {
            return new ResponseEntity<>("No shipments found for user.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    public ResponseEntity<?> getShipmentRecordsByowner(int userId) {
        List<ShipmentRecord> list = shipmentRecordRepository.findByOwnerId((long) userId);
        if (list.isEmpty()) {
            return new ResponseEntity<>("No shipments found for user.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
