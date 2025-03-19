package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.domain.ShipmentRecord;
import chernandez.blockedsupplybackend.domain.State;
import chernandez.blockedsupplybackend.domain.User;
import chernandez.blockedsupplybackend.domain.dto.ShipmentInput;
import chernandez.blockedsupplybackend.domain.dto.ShipmentOutput;
import chernandez.blockedsupplybackend.repositories.ShipmentRecordRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import smartContracts.web3.ShipmentManagement;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ShipmentService {

    private final ShipmentRecordRepository shipmentRecordRepository;
    private final BlockchainService blockchainService;
    private final AuthService authService;

    public ShipmentService(ShipmentRecordRepository shipmentRecordRepository, BlockchainService blockchainService, AuthService authService) {
        this.shipmentRecordRepository = shipmentRecordRepository;
        this.blockchainService = blockchainService;
        this.authService = authService;
    }

    public ResponseEntity<?> createShipment(@NotNull ShipmentInput shipmentInput) throws Exception {
        ShipmentManagement contract;
        try{
            contract = blockchainService.setCredentialsToContractInstance();
        } catch (Exception e){
           return new ResponseEntity<>("User does not have set a blockchain address.", HttpStatus.BAD_REQUEST);
        }

        User user = authService.getUserFromJWT();

        TransactionReceipt receipt;
        BigInteger units = BigInteger.valueOf(shipmentInput.getUnits());
        BigInteger weight = BigInteger.valueOf(shipmentInput.getWeight());

        AtomicLong shipmentId = new AtomicLong();
        AtomicReference<String> currentOwner = new AtomicReference<>();

        if (units.signum() < 0 || weight.signum() < 0) {
            return new ResponseEntity<>("Units and weight must be non-negative.", HttpStatus.BAD_REQUEST);
        }

        try {
            receipt = contract.createShipment(
                    shipmentInput.getName(),
                    shipmentInput.getDescription(),
                    shipmentInput.getOrigin(),
                    shipmentInput.getDestination(),
                    units,
                    weight
            ).send();

            contract.shipmentCreatedEventFlowable(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST)
                    .subscribe(event -> {
                        shipmentId.set(event.id.longValue());
                        currentOwner.set((event.currentOwner));
            });

        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create shipment: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ShipmentRecord shipmentRecord = new ShipmentRecord(shipmentId.get(), currentOwner.get(), State.CREATED, user.getId());
        shipmentRecordRepository.save(shipmentRecord);

        return new ResponseEntity<>(receipt, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getShipment(int shipmentId) throws Exception {
        ShipmentManagement contract;
        try{
            contract = blockchainService.setCredentialsToContractInstance();
        } catch (Exception e){
            return new ResponseEntity<>("User does not have set a blockchain address.", HttpStatus.BAD_REQUEST);
        }

        try {
            BigInteger id = BigInteger.valueOf(shipmentId);

            contract.getShipment(id).send();

            ShipmentOutput shipment = new ShipmentOutput();

            contract.shipmentRetrievedEventFlowable(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST)
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
        ShipmentManagement shipmentContract = blockchainService.setCredentialsToContractInstance();
        return new ResponseEntity<>(shipmentContract.getNextShipmentId().send(), HttpStatus.OK);
    }

}