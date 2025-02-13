package chernandez.blockedsupplybackend.controllers;

import chernandez.blockedsupplybackend.domain.dto.TransferInput;
import chernandez.blockedsupplybackend.domain.dto.TransferOutput;
import chernandez.blockedsupplybackend.services.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    private final TransferService shipmentService;

    public TransferController(TransferService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<TransactionReceipt> transferShipment(@RequestBody TransferInput request) {
        return shipmentService.transferShipment(request);
    }

    @GetMapping("/{shipmentId}")
    public ResponseEntity<List<TransferOutput>> getTransferHistory(@PathVariable int shipmentId) throws Exception {
        return shipmentService.getTransferHistory(shipmentId);
    }

    @GetMapping("/nextId")
    public ResponseEntity<BigInteger> getNextTransferId() throws Exception {
        return shipmentService.getNextTransfertId();
    }


}
