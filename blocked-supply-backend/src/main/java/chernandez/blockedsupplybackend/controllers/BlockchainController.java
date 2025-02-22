package chernandez.blockedsupplybackend.controllers;

import chernandez.blockedsupplybackend.services.BlockchainService;
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

    @PostMapping
    public String deployContract() throws Exception {
        return blockchainService.deployContract();
    }

    @GetMapping("/blockNumber")
    public String getBlockNumber() throws IOException {
        return "Latest Block Number: " + blockchainService.getLatestBlockNumber();
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "Blockchain Test successful!";
    }

}