package chernandez.blockedsupplybackend.domain.dto;

import chernandez.blockedsupplybackend.domain.State;
import lombok.Data;

@Data
public class TransferOutput {
    private int id;
    private int shipmentId;
    private int timestamp;
    private State newState;
    private String location;
    private String newOwner;
    private String transferNotes;

    public TransferOutput(int id, int shipmentId, int timestamp, State newState, String location, String newOwner, String transferNotes) {
        this.id = id;
        this.shipmentId = shipmentId;
        this.timestamp = timestamp;
        this.newState = newState;
        this.location = location;
        this.newOwner = newOwner;
        this.transferNotes = transferNotes;
    }
}
