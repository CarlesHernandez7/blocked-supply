package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.config.exceptions.BlockchainException;
import chernandez.blockedsupplybackend.domain.dto.TransferInput;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import smartContracts.web3.ShipmentManagement;

import java.math.BigInteger;

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

    public ResponseEntity<BigInteger> getNextTransfertId() throws Exception {
        return new ResponseEntity<>(shipmentContract.getNextTransferId().send(), org.springframework.http.HttpStatus.OK);
    }
}
