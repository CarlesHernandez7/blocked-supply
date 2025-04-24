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
    public ResponseEntity<?> transferShipment(@RequestBody TransferInput transferInput) throws Exception {
        return transferService.transferShipment(transferInput);
    }

    @GetMapping("/{sku}")
    public ResponseEntity<?> getTransferHistory(@PathVariable String sku) {
        return transferService.getTransferHistory(sku);
    }

    @GetMapping("/nextId")
    public ResponseEntity<?> getNextTransferId() {
        return transferService.getNextTransferId();
    }


}
