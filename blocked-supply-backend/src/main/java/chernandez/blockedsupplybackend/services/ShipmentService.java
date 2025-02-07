package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.config.exceptions.BlockchainException;
import chernandez.blockedsupplybackend.domain.ShipmentDTO;
import chernandez.blockedsupplybackend.domain.ShipmentOutput;
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

    public TransactionReceipt createShipment(ShipmentDTO shipmentDTO) {
        try {
            BigInteger units = BigInteger.valueOf(shipmentDTO.getUnits());
            BigInteger weight = BigInteger.valueOf(shipmentDTO.getWeight());

            if (units.signum() < 0 || weight.signum() < 0) {
                throw new IllegalArgumentException("Units and weight must be non-negative.");
            }

            return shipmentContract.createShipment(
                    shipmentDTO.getName(),
                    shipmentDTO.getDescription(),
                    shipmentDTO.getOrigin(),
                    shipmentDTO.getDestination(),
                    units,
                    weight
            ).send();
        } catch (Exception e) {
            throw new BlockchainException("Failed to create shipment", e);
        }
    }

    public ShipmentOutput getShipment(int shipmentId) throws Exception {

        BigInteger id = BigInteger.valueOf(shipmentId);

        shipmentContract.fetchShipment(id).send();

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
                });

        return shipment;
    }

    public BigInteger getNextShipmentId() throws Exception {
        return shipmentContract.getNextShipmentId().send();
    }

}
