package chernandez.blockedsupplybackend.controllers;

import chernandez.blockedsupplybackend.services.ShipmentRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/records")
public class ShipmentRecordController {

    private final ShipmentRecordService shipmentRecordService;

    public ShipmentRecordController(ShipmentRecordService shipmentRecordService) {
        this.shipmentRecordService = shipmentRecordService;
    }

    @GetMapping("/shipment/{shipmentId}")
    public ResponseEntity<?> getShipmentRecord(@PathVariable int shipmentId) {
        return shipmentRecordService.getShipmentRecord(shipmentId);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllShipmentRecords() {
        return shipmentRecordService.getAllShipmentRecords();
    }

    @GetMapping("/progress")
    public ResponseEntity<?> getShipmentRecordsInProgress() {
        return shipmentRecordService.getShipmentRecordsInProgress();
    }

    @GetMapping("/delivered")
    public ResponseEntity<?> getShipmentRecordsDeliveredToday() {
        return shipmentRecordService.getShipmentRecordsDeliveredToday();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getShipmentRecordsByParticipant(@PathVariable int userId) {
        return shipmentRecordService.getShipmentRecordsByParticipant(userId);
    }


}
