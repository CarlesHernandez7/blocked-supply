package chernandez.blockedsupplybackend.controllers;

import chernandez.blockedsupplybackend.domain.dto.ShipmentInput;
import chernandez.blockedsupplybackend.services.ShipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipment")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createShipment(@RequestBody ShipmentInput shipmentInput) throws Exception {
        return shipmentService.createShipment(shipmentInput);
    }

    @GetMapping("/{shipmentId}")
    public ResponseEntity<?> getShipment(@PathVariable int shipmentId) {
        return shipmentService.getShipment(shipmentId);
    }

    @GetMapping("/nextId")
    public ResponseEntity<?> getNextShipmentId() {
        return shipmentService.getNextShipmentId();
    }
}
