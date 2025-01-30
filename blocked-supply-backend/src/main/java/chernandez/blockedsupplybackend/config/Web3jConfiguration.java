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

    @Value("${contract.address}")
    private String contractAddress;

    @Value("${contract.private-key}")
    private String privateKey;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(web3ClientUrl));
    }

    @Bean
    public ShipmentManagement shipmentManagement(Web3j web3j) {
        return ShipmentManagement.load(
                contractAddress,
                web3j,
                Credentials.create(privateKey),
                new DefaultGasProvider()
        );
    }
}
