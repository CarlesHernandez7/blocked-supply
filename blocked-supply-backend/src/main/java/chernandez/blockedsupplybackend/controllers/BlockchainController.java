package chernandez.blockedsupplybackend.controllers;

import chernandez.blockedsupplybackend.services.BlockchainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/blockchain")
public class BlockchainController {

    private final BlockchainService blockchainService;

    public BlockchainController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @PostMapping()
    public ResponseEntity<String> deployContract() throws Exception {
        System.out.println("🚀 Blockchain API called!");
        return blockchainService.deployContract();
    }

    @GetMapping("/blockNumber")
    public ResponseEntity<Long> getBlockNumber() throws IOException {
        return blockchainService.getLatestBlockNumber();
    }
}