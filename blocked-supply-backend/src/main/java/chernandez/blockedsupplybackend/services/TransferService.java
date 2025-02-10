package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.config.exceptions.BlockchainException;
import chernandez.blockedsupplybackend.domain.dto.TransferInput;
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

    public TransactionReceipt transferShipment(TransferInput request) {
        BigInteger id = BigInteger.valueOf(request.getShipmentId());

        try {
            return shipmentContract
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
    }

    public BigInteger getNextTransfertId() throws Exception {
        return shipmentContract.getNextTransferId().send();
    }
}
