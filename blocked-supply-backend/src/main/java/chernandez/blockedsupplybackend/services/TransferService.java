package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.domain.State;
import chernandez.blockedsupplybackend.domain.dto.ErrorResponseDTO;
import chernandez.blockedsupplybackend.domain.dto.TransferInput;
import chernandez.blockedsupplybackend.domain.dto.TransferOutput;
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

    public ResponseEntity<?> transferShipment(TransferInput request) {
        TransactionReceipt receipt;
        BigInteger id = BigInteger.valueOf(request.getShipmentId());

        try {
            receipt = shipmentContract
                    .shipmentTransfer(
                            id,
                            request.getNewShipmentOwner(),
                            BigInteger.valueOf(request.getNewState()),
                            request.getTransferNotes()
                    )
                    .send();
        } catch (Exception e) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO("Blockchain Error", "Failed to create transfer for the shipment: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(receipt, HttpStatus.CREATED);
    }

    public ResponseEntity<List<TransferOutput>> getTransferHistory(int shipmentId) throws Exception {
        BigInteger shipmentIdBigInt = BigInteger.valueOf(shipmentId);

        BigInteger transferCount = shipmentContract.getTransferCount(shipmentIdBigInt).send();

        List<TransferOutput> transfers = new ArrayList<>();

        for (BigInteger i = BigInteger.ZERO; i.compareTo(transferCount) < 0; i = i.add(BigInteger.ONE)) {
            TransferOutput transfer = new TransferOutput();

            shipmentContract.getTransferByIndex(shipmentIdBigInt, i).send();

            shipmentContract.transferRetrievedEventFlowable(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST)
                    .subscribe(event -> {
                        transfer.setId(event.id.intValue());
                        transfer.setShipmentId(event.shipmentId.intValue());
                        transfer.setTimestamp(event.timestamp.intValue());
                        transfer.setNewState(State.fromBigInt(event.newState));
                        transfer.setNewOwner(event.newShipmentOwner);
                        transfer.setTransferNotes(event.transferNotes);
                    });

            transfers.add(transfer);
        }

        return new ResponseEntity<>(transfers, HttpStatus.OK);
    }

    public ResponseEntity<BigInteger> getNextTransfertId() throws Exception {
        return new ResponseEntity<>(shipmentContract.getNextTransferId().send(), HttpStatus.OK);
    }
}