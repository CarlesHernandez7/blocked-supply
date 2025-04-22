package chernandez.blockedsupplybackend.controllers;

import chernandez.blockedsupplybackend.services.ShipmentRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getShipmentStatistics() {
        return shipmentRecordService.getShipmentStatistics();
    }

    @GetMapping("/participant")
    public ResponseEntity<?> getShipmentRecordsByParticipant() {
        return shipmentRecordService.getShipmentRecordsByParticipant();
    }

    @GetMapping("/owner")
    public ResponseEntity<?> getShipmentRecordsByowner() {
        return shipmentRecordService.getShipmentRecordsByOwner();
    }


}
