package chernandez.blockedsupplybackend.controllers;

import chernandez.blockedsupplybackend.domain.ShipmentInput;
import chernandez.blockedsupplybackend.domain.ShipmentOutput;
import chernandez.blockedsupplybackend.services.ShipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/shipment")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<TransactionReceipt> createShipment(@RequestBody ShipmentInput shipmentInput) {
        TransactionReceipt receipt = shipmentService.createShipment(shipmentInput);
        return ResponseEntity.status(201).body(receipt);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentOutput> getShipment(@PathVariable int id) throws Exception {
        return ResponseEntity.ok(shipmentService.getShipment(id));
    }

    @GetMapping("/nextId")
    public ResponseEntity<BigInteger> getNextShipmentId() throws Exception {
        return ResponseEntity.ok(shipmentService.getNextShipmentId());
    }
}
