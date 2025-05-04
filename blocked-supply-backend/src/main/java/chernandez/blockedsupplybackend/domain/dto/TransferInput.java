package chernandez.blockedsupplybackend.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransferInput {
    private int shipmentId;
    private String newShipmentOwner;
    private int newState;
    private String location;
    private String transferNotes;
    private String from;

    public TransferInput(int shipmentId, String newShipmentOwner, int newState, String location, String transferNotes) {
        this.shipmentId = shipmentId;
        this.newShipmentOwner = newShipmentOwner;
        this.newState = newState;
        this.location = location;
        this.transferNotes = transferNotes;
    }
}
