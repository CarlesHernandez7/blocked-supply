package chernandez.blockedsupplybackend.domain.dto;

import lombok.Data;

@Data
public class TransferInput {

    private int shipmentId;
    private String newShipmentOwner;
    private int newState;
    private String transferNotes;

    public TransferInput() {
    }

    public TransferInput(int shipmentId, String newShipmentOwner, int newState, String transferNotes) {
        this.shipmentId = shipmentId;
        this.newShipmentOwner = newShipmentOwner;
        this.newState = newState;
        this.transferNotes = transferNotes;
    }
}
