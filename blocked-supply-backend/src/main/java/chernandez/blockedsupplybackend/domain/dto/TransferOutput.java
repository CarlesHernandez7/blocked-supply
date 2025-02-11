package chernandez.blockedsupplybackend.domain.dto;

import chernandez.blockedsupplybackend.domain.State;
import lombok.Data;

@Data
public class TransferOutput {
    int transferId;
    int shipmentId;
    String timestamp;
    State newState;
    String newShipmentOwner;
    String transferNotes;

    public TransferOutput() {
    }

    public TransferOutput(int transferId, int shipmentId, String timestamp, State newState, String newShipmentOwner, String transferNotes) {
        this.transferId = transferId;
        this.shipmentId = shipmentId;
        this.timestamp = timestamp;
        this.newState = newState;
        this.newShipmentOwner = newShipmentOwner;
        this.transferNotes = transferNotes;
    }
}
