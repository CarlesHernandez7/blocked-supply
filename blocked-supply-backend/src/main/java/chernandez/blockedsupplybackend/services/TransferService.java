package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.domain.State;
import chernandez.blockedsupplybackend.domain.dto.TransferInput;
import chernandez.blockedsupplybackend.domain.dto.TransferOutput;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class TransferService {

    private final Web3j web3j;
    private final Credentials credentials;
    private ShipmentManagement shipmentContract;

    public TransferService(Web3j web3j, Credentials credentials) {
        this.web3j = web3j;
        this.credentials = credentials;
    }

    public void setContractAddress(String contractAddress) {
        this.shipmentContract = ShipmentManagement.load(contractAddress, web3j, credentials, new DefaultGasProvider());
    }

    public ResponseEntity<?> transferShipment(@NotNull TransferInput request) {
        TransactionReceipt receipt;
        BigInteger id = BigInteger.valueOf(request.getShipmentId());

        try {
            receipt = shipmentContract
                    .shipmentTransfer(
                            id,
                            request.getNewShipmentOwner(),
                            BigInteger.valueOf(request.getNewState()),
                            request.getLocation(),
                            request.getTransferNotes()
                    )
                    .send();
        } catch (Exception e) {
            if (e.getMessage().contains("Only the current owner can perform this action.")) {
                return new ResponseEntity<>("Only the current owner can perform this action.", HttpStatus.FORBIDDEN);
            }
            else {
                return new ResponseEntity<>("Failed to create transfer for the shipment: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(receipt, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getTransferHistory(int shipmentId) {
        BigInteger shipmentIdBigInt = BigInteger.valueOf(shipmentId);

        List<TransferOutput> transfers;

        try {

            BigInteger transferCount = shipmentContract.getTransferCount(shipmentIdBigInt).send();

            transfers = new ArrayList<>();

            for (BigInteger i = BigInteger.ZERO; i.compareTo(transferCount) < 0; i = i.add(BigInteger.ONE)) {
                TransferOutput transfer = new TransferOutput();

                shipmentContract.getTransferByIndex(shipmentIdBigInt, i).send();

                shipmentContract.transferRetrievedEventFlowable(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST)
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
        return new ResponseEntity<>(shipmentContract.getNextTransferId().send(), HttpStatus.OK);
    }
}