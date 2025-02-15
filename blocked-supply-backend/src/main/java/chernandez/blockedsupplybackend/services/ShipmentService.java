package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.config.exceptions.BlockchainException;
import chernandez.blockedsupplybackend.domain.State;
import chernandez.blockedsupplybackend.domain.dto.ShipmentInput;
import chernandez.blockedsupplybackend.domain.dto.ShipmentOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;
import smartContracts.web3.ShipmentManagement;

import java.math.BigInteger;

@Service
public class ShipmentService {

    private ShipmentManagement shipmentContract;
    private final Web3j web3j;
    private final Credentials credentials;

    public ShipmentService(Web3j web3j, Credentials credentials) {
        this.web3j = web3j;
        this.credentials = credentials;
    }

    public void setContractAddress(String contractAddress) {
        this.shipmentContract = ShipmentManagement.load(contractAddress, web3j, credentials, new DefaultGasProvider());
    }

    public ResponseEntity<TransactionReceipt> createShipment(ShipmentInput shipmentInput) {
        TransactionReceipt receipt;
        try {
            BigInteger units = BigInteger.valueOf(shipmentInput.getUnits());
            BigInteger weight = BigInteger.valueOf(shipmentInput.getWeight());

            if (units.signum() < 0 || weight.signum() < 0) {
                throw new IllegalArgumentException("Units and weight must be non-negative.");
            }

            receipt = shipmentContract.createShipment(
                    shipmentInput.getName(),
                    shipmentInput.getDescription(),
                    shipmentInput.getOrigin(),
                    shipmentInput.getDestination(),
                    units,
                    weight
            ).send();
        } catch (Exception e) {
            throw new BlockchainException("Failed to create shipment", e);
        }
        return new ResponseEntity<>(receipt, org.springframework.http.HttpStatus.CREATED);
    }

    public ResponseEntity<ShipmentOutput> getShipment(int shipmentId) throws Exception {

        BigInteger id = BigInteger.valueOf(shipmentId);

        shipmentContract.getShipment(id).send();

        ShipmentOutput shipment = new ShipmentOutput();

        shipmentContract.shipmentRetrievedEventFlowable(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST)
                .subscribe(event -> {
                    shipment.setId(event.id.intValue());
                    shipment.setName(event.name);
                    shipment.setDescription(event.description);
                    shipment.setOrigin(event.origin);
                    shipment.setDestination(event.destination);
                    shipment.setUnits(event.units.intValue());
                    shipment.setWeight(event.weight.intValue());
                    shipment.setCurrentState(State.fromBigInt(event.currentState));
                    shipment.setCurrentOwner(event.currentOwner);
                });

        return new ResponseEntity<>(shipment, org.springframework.http.HttpStatus.OK);
    }

    public ResponseEntity<BigInteger> getNextShipmentId() throws Exception {
        return new ResponseEntity<>(shipmentContract.getNextShipmentId().send(), org.springframework.http.HttpStatus.OK);
    }

}