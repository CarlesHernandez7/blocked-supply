package chernandez.blockedsupplybackend.controllers;

import chernandez.blockedsupplybackend.domain.Shipment;
import chernandez.blockedsupplybackend.dto.ShipmentDTO;
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
    public ResponseEntity<TransactionReceipt> createShipment(@RequestBody ShipmentDTO shipmentDTO) throws Exception {
        TransactionReceipt receipt = shipmentService.createShipment(shipmentDTO);
        return ResponseEntity.status(201).body(receipt);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shipment> getShipment(@PathVariable BigInteger id) {
        return ResponseEntity.ok(shipmentService.getShipment(id));
    }
}
