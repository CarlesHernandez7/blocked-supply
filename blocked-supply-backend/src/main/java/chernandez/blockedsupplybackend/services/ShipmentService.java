package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.config.exceptions.BlockchainException;
import chernandez.blockedsupplybackend.domain.dto.ShipmentInput;
import chernandez.blockedsupplybackend.domain.dto.ShipmentOutput;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import smartContracts.web3.ShipmentManagement;

import java.math.BigInteger;

@Service
public class ShipmentService {

    private final ShipmentManagement shipmentContract;

    public ShipmentService(ShipmentManagement shipmentContract) {
        this.shipmentContract = shipmentContract;
    }

    public TransactionReceipt createShipment(ShipmentInput shipmentInput) {
        try {
            BigInteger units = BigInteger.valueOf(shipmentInput.getUnits());
            BigInteger weight = BigInteger.valueOf(shipmentInput.getWeight());

            if (units.signum() < 0 || weight.signum() < 0) {
                throw new IllegalArgumentException("Units and weight must be non-negative.");
            }

            return shipmentContract.createShipment(
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
    }

    public ShipmentOutput getShipment(int shipmentId) throws Exception {

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
                    shipment.setCurrentState(ShipmentOutput.State.fromInt(event.currentState));
                    shipment.setCurrentOwner(event.currentOwner);
                });

        return shipment;
    }

    public BigInteger getNextShipmentId() throws Exception {
        return shipmentContract.getNextShipmentId().send();
    }

}
