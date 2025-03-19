package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final AuthService authService;
    private String contractAddress;

    public BlockchainService(Web3j web3j, Credentials credentials, AuthService authService) {
        this.web3j = web3j;
        this.credentials = credentials;
        this.authService = authService;
    }

    public ResponseEntity<String> deployContract() throws Exception {
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

        this.contractAddress = shipmentManagement.getContractAddress();
        System.out.println("Contract deployed at address: " + this.contractAddress);

        return new ResponseEntity<>(contractAddress, HttpStatus.CREATED);
    }

    public ResponseEntity<Long> getLatestBlockNumber() throws IOException {
        EthBlockNumber blockNumber = web3j.ethBlockNumber().send();
        return new ResponseEntity<>(blockNumber.getBlockNumber().longValue(), HttpStatus.OK);
    }

    public ShipmentManagement setCredentialsToContractInstance() throws Exception {
        User user = authService.getUserFromJWT();
        String userAddress = user.getBlockchainAddress();
        ShipmentManagement shipmentContract;

        if (userAddress == null) {
            throw new Exception("User does not have set a blockchain address.");
        } else {
            try {
                Credentials newCredentials = Credentials.create(user.getBlockchainKey());

                shipmentContract = ShipmentManagement.load(
                        this.contractAddress,
                        web3j,
                        newCredentials,
                        new DefaultGasProvider()
                );
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }

        return ShipmentManagement.load(
                shipmentContract.getContractAddress(),
                web3j,
                credentials,
                new DefaultGasProvider()
        );
    }
}
