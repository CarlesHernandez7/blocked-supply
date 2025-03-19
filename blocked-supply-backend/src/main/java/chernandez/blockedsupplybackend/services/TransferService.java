package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.domain.ShipmentRecord;
import chernandez.blockedsupplybackend.domain.State;
import chernandez.blockedsupplybackend.domain.User;
import chernandez.blockedsupplybackend.domain.dto.TransferInput;
import chernandez.blockedsupplybackend.domain.dto.TransferOutput;
import chernandez.blockedsupplybackend.repositories.ShipmentRecordRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import smartContracts.web3.ShipmentManagement;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class TransferService {

    private final ShipmentRecordRepository shipmentRecordRepository;
    private final BlockchainService blockchainService;
    private final AuthService authService;

    public TransferService(ShipmentRecordRepository shipmentRecordRepository, BlockchainService blockchainService, AuthService authService) {
        this.shipmentRecordRepository = shipmentRecordRepository;
        this.blockchainService = blockchainService;
        this.authService = authService;
    }

    public ResponseEntity<?> transferShipment(@NotNull TransferInput request) throws Exception {
        ShipmentManagement contract = blockchainService.setCredentialsToContractInstance();
        User user = authService.getUserFromJWT();

        TransactionReceipt receipt;
        BigInteger id = BigInteger.valueOf(request.getShipmentId());

        AtomicLong shipmentId = new AtomicLong();
        AtomicReference<BigInteger> newState = new AtomicReference<>();

        try {
            receipt = contract.shipmentTransfer(
                    id,
                    user.getBlockchainAddress(),
                    BigInteger.valueOf(request.getNewState()),
                    request.getLocation(),
                    request.getTransferNotes()
                    )
            .send();

            contract.transferCreatedEventFlowable(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST)
                    .subscribe(event -> {
                        shipmentId.set(event.shipmentId.longValue());
                        newState.set(event.newState);
            });

        } catch (Exception e) {
            if (e.getMessage().contains("Only the current owner can perform this action.")) {
                return new ResponseEntity<>("Only the current owner can perform this action.", HttpStatus.FORBIDDEN);
            }
            else {
                return new ResponseEntity<>("Failed to create transfer for the shipment: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        Optional<ShipmentRecord> optionalShipmentRecord = shipmentRecordRepository.findById(shipmentId.get());
        if (optionalShipmentRecord.isEmpty()) {
            return new ResponseEntity<>("Shipment record not found", HttpStatus.NOT_FOUND);
        }

        ShipmentRecord shipmentRecord = optionalShipmentRecord.get();
        shipmentRecord.setStatus(State.fromBigInt(newState.get()));
        shipmentRecord.addParticipant(user.getId());
        shipmentRecordRepository.save(shipmentRecord);

        return new ResponseEntity<>(receipt, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getTransferHistory(int shipmentId) throws Exception {
        ShipmentManagement contract = blockchainService.setCredentialsToContractInstance();

        BigInteger shipmentIdBigInt = BigInteger.valueOf(shipmentId);

        List<TransferOutput> transfers;

        try {

            BigInteger transferCount = contract.getTransferCount(shipmentIdBigInt).send();

            transfers = new ArrayList<>();

            for (BigInteger i = BigInteger.ZERO; i.compareTo(transferCount) < 0; i = i.add(BigInteger.ONE)) {
                TransferOutput transfer = new TransferOutput();

                contract.getTransferByIndex(shipmentIdBigInt, i).send();

                contract.transferRetrievedEventFlowable(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST)
                        .subscribe(event -> {
                            transfer.setId(event.id.intValue());
                            transfer.setShipmentId(event.shipmentId.intValue());
                            transfer.setTimestamp(event.timestamp.intValue());
                            transfer.setNewState(State.fromBigInt(event.newState));
                            transfer.setLocation(event.location);
                            transfer.setNewOwner(event.newShipmentOwner);
                            transfer.setTransferNotes(event.transferNotes);
                        });

                transfers.add(transfer);
            }
        } catch (Exception e) {
            if (e.getMessage().contains("Shipment ID must be greater than 0")) {
                return new ResponseEntity<>("Invalid Shipment ID: Must be greater than 0.", HttpStatus.BAD_REQUEST);
            } else if (e.getMessage().contains("Shipment does not exist")) {
                return new ResponseEntity<>("Shipment not found.", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("An error occurred while retrieving the shipment.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(transfers, HttpStatus.OK);
    }

    public ResponseEntity<BigInteger> getNextTransfertId() throws Exception {
        ShipmentManagement shipmentContract = blockchainService.setCredentialsToContractInstance();
        return new ResponseEntity<>(shipmentContract.getNextTransferId().send(), HttpStatus.OK);
    }
}