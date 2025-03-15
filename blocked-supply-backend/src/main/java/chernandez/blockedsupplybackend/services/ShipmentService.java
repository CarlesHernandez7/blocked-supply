package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.domain.ShipmentRecord;
import chernandez.blockedsupplybackend.domain.State;
import chernandez.blockedsupplybackend.domain.dto.ShipmentInput;
import chernandez.blockedsupplybackend.domain.dto.ShipmentOutput;
import chernandez.blockedsupplybackend.repositories.ShipmentRecordRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;
import smartContracts.web3.ShipmentManagement;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ShipmentService {

    private final Web3j web3j;
    private final Credentials credentials;
    private ShipmentManagement shipmentContract;
    private final ShipmentRecordRepository shipmentRecordRepository;

    public ShipmentService(Web3j web3j, Credentials credentials, ShipmentRecordRepository shipmentRecordRepository) {
        this.web3j = web3j;
        this.credentials = credentials;
        this.shipmentRecordRepository = shipmentRecordRepository;
    }

    public void setContractAddress(String contractAddress) {
        this.shipmentContract = ShipmentManagement.load(contractAddress, web3j, credentials, new DefaultGasProvider());
    }

    public ResponseEntity<?> createShipment(@NotNull ShipmentInput shipmentInput) {
        TransactionReceipt receipt;
        BigInteger units = BigInteger.valueOf(shipmentInput.getUnits());
        BigInteger weight = BigInteger.valueOf(shipmentInput.getWeight());

        AtomicLong shipmentId = new AtomicLong();
        AtomicReference<String> currentOwner = new AtomicReference<>();

        if (units.signum() < 0 || weight.signum() < 0) {
            return new ResponseEntity<>("Units and weight must be non-negative.", HttpStatus.BAD_REQUEST);
        }

        try {
            receipt = shipmentContract.createShipment(
                    shipmentInput.getName(),
                    shipmentInput.getDescription(),
                    shipmentInput.getOrigin(),
                    shipmentInput.getDestination(),
                    units,
                    weight
            ).send();

            shipmentContract.shipmentCreatedEventFlowable(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST)
                    .subscribe(event -> {
                        shipmentId.set(event.id.longValue());
                        currentOwner.set((event.currentOwner));
            });

        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create shipment: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // TODO UPDATE owener id WHEN JWT AUTHENTICATION IS IMPLEMENTED
        ShipmentRecord shipmentRecord = new ShipmentRecord(shipmentId.get(), currentOwner.get(), State.CREATED, 1L);

        shipmentRecordRepository.save(shipmentRecord);

        return new ResponseEntity<>(receipt, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getShipment(int shipmentId) {
        try {
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

            return new ResponseEntity<>(shipment, HttpStatus.OK);

        } catch (Exception e) {
            if (e.getMessage().contains("Shipment ID must be greater than 0")) {
                return new ResponseEntity<>("Invalid Shipment ID: Must be greater than 0.", HttpStatus.BAD_REQUEST);
            } else if (e.getMessage().contains("Shipment does not exist")) {
                return new ResponseEntity<>("Shipment not found.", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("An error occurred while retrieving the shipment.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }


    public ResponseEntity<BigInteger> getNextShipmentId() throws Exception {
        return new ResponseEntity<>(shipmentContract.getNextShipmentId().send(), HttpStatus.OK);
    }

}