package chernandez.blockedsupplybackend.controllers;

import chernandez.blockedsupplybackend.domain.dto.ShipmentInput;
import chernandez.blockedsupplybackend.services.ShipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/shipment")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createShipment(@RequestBody ShipmentInput shipmentInput) {
        return shipmentService.createShipment(shipmentInput);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getShipment(@PathVariable int id) {
        return shipmentService.getShipment(id);
    }

    @GetMapping("/{ownerAddress}/owner")
    public ResponseEntity<?> getShipmentsByOwner(@PathVariable String ownerAddress) {
        return shipmentService.getShipmentsByOwner(ownerAddress);
    }

    @GetMapping("/nextId")
    public ResponseEntity<BigInteger> getNextShipmentId() throws Exception {
        return shipmentService.getNextShipmentId();
    }
}
