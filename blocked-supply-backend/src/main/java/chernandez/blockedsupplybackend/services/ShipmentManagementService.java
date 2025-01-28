package chernandez.blockedsupplybackend.services;

import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;

@Service
public class ShipmentManagementService {

    private final Web3j web3j;

    public ShipmentManagementService(Web3j web3j) {
        this.web3j = web3j;
    }


}
