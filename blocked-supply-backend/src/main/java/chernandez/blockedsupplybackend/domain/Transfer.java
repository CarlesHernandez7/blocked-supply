package chernandez.blockedsupplybackend.domain;

import lombok.Data;

@Data
public class Transfer {

    private int id;
    private int shipmentId;
    private int timestamp;
    private String newShipmentOwner;
    private String transferNotes;

    public Transfer(int id, int shipmentId, int timestamp, String newShipmentOwner, String transferNotes) {
        this.id = id;
        this.shipmentId = shipmentId;
        this.timestamp = timestamp;
        this.newShipmentOwner = newShipmentOwner;
        this.transferNotes = transferNotes;
    }
}
