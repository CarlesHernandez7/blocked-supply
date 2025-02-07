package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.config.exceptions.BlockchainException;
import chernandez.blockedsupplybackend.config.exceptions.ShipmentNotFoundException;
import chernandez.blockedsupplybackend.domain.Shipment;
import chernandez.blockedsupplybackend.dto.ShipmentDTO;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple9;
import smartContracts.web3.ShipmentManagement;

import java.math.BigInteger;

@Service
public class ShipmentService {

    private final ShipmentManagement shipmentContract;

    public ShipmentService(ShipmentManagement shipmentContract) {
        this.shipmentContract = shipmentContract;
    }

    public TransactionReceipt createShipment(ShipmentDTO shipmentDTO) throws Exception {
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

    public Shipment getShipment(BigInteger shipmentId) {
        try {
            Tuple9<BigInteger, String, String, String, String, BigInteger, BigInteger, BigInteger, String> shipmentTuple =
                    shipmentContract.getShipment(shipmentId).send();

            if (shipmentTuple == null || shipmentTuple.component1().equals(BigInteger.ZERO)) {
                throw new ShipmentNotFoundException("Shipment not found with ID: " + shipmentId);
            }

            return new Shipment(
                    shipmentTuple.component1().intValue(),  // id
                    shipmentTuple.component2(),  // name
                    shipmentTuple.component3(),  // description
                    shipmentTuple.component4(),  // origin
                    shipmentTuple.component5(),  // destination
                    shipmentTuple.component6().intValue(),  // units
                    shipmentTuple.component7().intValue(),  // weight
                    Shipment.State.fromInt(shipmentTuple.component8().intValue()), // Convert int to Enum
                    shipmentTuple.component9()   // currentOwner
            );
        } catch (ShipmentNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new BlockchainException("Error retrieving shipment", e);
        }
    }

}
