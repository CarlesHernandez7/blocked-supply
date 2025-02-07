package chernandez.blockedsupplybackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import smartContracts.web3.ShipmentManagement;

@Configuration
public class Web3jConfiguration {

    @Value("${web3j.client-address}")
    private String web3ClientUrl;

    @Value("${contract.private-key}")
    private String privateKey;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(web3ClientUrl));
    }

    @Bean
    public ShipmentManagement shipmentManagement(Web3j web3j) throws Exception {
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
        return ShipmentManagement.load(contractAddress, web3j, credentials, new DefaultGasProvider());
    }
}
