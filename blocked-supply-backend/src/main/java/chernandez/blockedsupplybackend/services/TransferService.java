package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.config.exceptions.BlockchainException;
import chernandez.blockedsupplybackend.domain.State;
import chernandez.blockedsupplybackend.domain.dto.TransferInput;
import chernandez.blockedsupplybackend.domain.dto.TransferOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import smartContracts.web3.ShipmentManagement;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransferService {

    private final ShipmentManagement shipmentContract;

    public TransferService(ShipmentManagement shipmentContract) {
        this.shipmentContract = shipmentContract;
    }

    public ResponseEntity<TransactionReceipt> transferShipment(TransferInput request) {
        BigInteger id = BigInteger.valueOf(request.getShipmentId());

        TransactionReceipt receipt;
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
            throw new BlockchainException("Error processing shipment transfer", e);
        }
        return new ResponseEntity<>(receipt, org.springframework.http.HttpStatus.CREATED);
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
        return new ResponseEntity<>(shipmentContract.getNextTransferId().send(), org.springframework.http.HttpStatus.OK);
    }
}
