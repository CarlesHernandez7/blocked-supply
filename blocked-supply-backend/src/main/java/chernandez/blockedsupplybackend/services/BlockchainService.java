package chernandez.blockedsupplybackend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.tx.gas.DefaultGasProvider;
import smartContracts.web3.ShipmentManagement;

import java.io.IOException;
import java.math.BigInteger;

@Service
public class BlockchainService {

    private final Web3j web3j;
    private final Credentials credentials;
    private final ShipmentService shipmentService;
    private final TransferService transferService;

    public BlockchainService(Web3j web3j, Credentials credentials, ShipmentService shipmentService, TransferService transferService) {
        this.web3j = web3j;
        this.credentials = credentials;
        this.shipmentService = shipmentService;
        this.transferService = transferService;
    }

    public String deployContract() throws Exception {
        ShipmentManagement shipmentManagement = ShipmentManagement.deploy(
                web3j,
                credentials,
                new DefaultGasProvider() {
                    @Override
                    public BigInteger getGasPrice() {
                        return DefaultGasProvider.GAS_PRICE;
                    }
                }
        ).send();

        String contractAddress = shipmentManagement.getContractAddress();
        System.out.println("Contract deployed at address: " + contractAddress);

        shipmentService.setContractAddress(contractAddress);
        transferService.setContractAddress(contractAddress);

        return contractAddress;
    }

    public long getLatestBlockNumber() throws IOException {
        EthBlockNumber blockNumber = web3j.ethBlockNumber().send();
        return blockNumber.getBlockNumber().longValue();
    }
}
