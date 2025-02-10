package chernandez.blockedsupplybackend.controllers;

import chernandez.blockedsupplybackend.domain.dto.ShipmentInput;
import chernandez.blockedsupplybackend.domain.dto.ShipmentOutput;
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
        return shipmentService.createShipment(shipmentInput);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentOutput> getShipment(@PathVariable int id) throws Exception {
        return shipmentService.getShipment(id);
    }

    @GetMapping("/nextId")
    public ResponseEntity<BigInteger> getNextShipmentId() throws Exception {
        return shipmentService.getNextShipmentId();
    }
}
