package chernandez.blockedsupplybackend.controllers;

import chernandez.blockedsupplybackend.domain.dto.TransferInput;
import chernandez.blockedsupplybackend.services.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> transferShipment(@RequestBody TransferInput transferInput) {
        return transferService.transferShipment(transferInput);
    }

    @GetMapping("/{shipmentId}")
    public ResponseEntity<?> getTransferHistory(@PathVariable int shipmentId) {
        return transferService.getTransferHistory(shipmentId);
    }

    @GetMapping("/nextId")
    public ResponseEntity<?> getNextTransferId() {
        return transferService.getNextTransferId();
    }


}
