package chernandez.blockedsupplybackend.domain.dto;

import lombok.Data;

@Data
public class TransferInput {

    private int shipmentId;
    private long newShipmentOwner;
    private int newState;
    private String location;
    private String transferNotes;

    public TransferInput() {
    }

    public TransferInput(int shipmentId, long newShipmentOwner, int newState, String location, String transferNotes) {
        this.shipmentId = shipmentId;
        this.newShipmentOwner = newShipmentOwner;
        this.newState = newState;
        this.location = location;
        this.transferNotes = transferNotes;
    }
}
