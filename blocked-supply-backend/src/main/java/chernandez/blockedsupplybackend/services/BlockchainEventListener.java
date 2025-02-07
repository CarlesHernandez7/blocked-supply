package chernandez.blockedsupplybackend.services;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.DefaultBlockParameterName;
import smartContracts.web3.ShipmentManagement;

@Service
public class BlockchainEventListener {

    private final ShipmentManagement shipmentContract;

    public BlockchainEventListener(ShipmentManagement shipmentContract) {
        this.shipmentContract = shipmentContract;
    }

    @PostConstruct
    public void listenToShipmentCreatedEvents() {
    /*
        shipmentContract.shipmentCreatedEventFlowable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST)
                .subscribe(event -> {
                    System.out.println("New Shipment Created:");
                    System.out.println("Shipment ID: " + event.shipmentId);
                    System.out.println("Product Name: " + event.name);
                    System.out.println("Owner: " + event.owner);
                }, error -> {
                    System.err.println("Error in event listener: " + error.getMessage());
                });

     */
    }
}
