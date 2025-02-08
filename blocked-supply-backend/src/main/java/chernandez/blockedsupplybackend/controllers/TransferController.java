package chernandez.blockedsupplybackend.controllers;

import chernandez.blockedsupplybackend.domain.TransferInput;
import chernandez.blockedsupplybackend.services.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    private final TransferService shipmentService;

    public TransferController(TransferService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<TransactionReceipt> transferShipment(@RequestBody TransferInput request) {
        TransactionReceipt receipt = shipmentService.transferShipment(request);
        return ResponseEntity.status(201).body(receipt);
    }

    @GetMapping("/nextId")
    public ResponseEntity<BigInteger> getNextTransferId() throws Exception {
        return ResponseEntity.ok(shipmentService.getNextTransfertId());
    }


}
