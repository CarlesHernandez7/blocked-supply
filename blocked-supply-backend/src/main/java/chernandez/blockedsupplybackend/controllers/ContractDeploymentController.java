package chernandez.blockedsupplybackend.controllers;

import chernandez.blockedsupplybackend.services.ShipmentService;
import chernandez.blockedsupplybackend.services.TransferService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.DefaultGasProvider;
import smartContracts.web3.ShipmentManagement;

@RestController
@RequestMapping("/api/deploy")
public class ContractDeploymentController {

    private final Web3j web3j;
    private final ShipmentService shipmentService;
    private final TransferService transferService;
    @Value("${contract.private-key}")
    private String privateKey;

    public ContractDeploymentController(Web3j web3j, ShipmentService shipmentService, TransferService transferService) {
        this.web3j = web3j;
        this.shipmentService = shipmentService;
        this.transferService = transferService;
    }

    @PostMapping
    public String deployContract() throws Exception {
        if (privateKey == null || privateKey.isEmpty()) {
            throw new RuntimeException("Private key is required but not set.");
        }

        Credentials credentials = Credentials.create(privateKey);
        System.out.println("Deploying contract with private key: " + privateKey);

        ShipmentManagement shipmentManagement = ShipmentManagement.deploy(
                web3j,
                credentials,
                new DefaultGasProvider()
        ).send();

        String contractAddress = shipmentManagement.getContractAddress();
        System.out.println("Contract deployed at address: " + contractAddress);

        shipmentService.setContractAddress(contractAddress);
        transferService.setContractAddress(contractAddress);

        return contractAddress;
    }
}