package chernandez.blockedsupplybackend.domain;

import lombok.Data;

@Data
public class Shipment {

    private int id;
    private String name;
    private String description;
    private String origin;
    private String destination;
    private int units;
    private int weight;
    private States currentState;
    private String currentOwner;
    private int[] transferHistory;

    public Shipment(int id, String name, String description, String origin, String destination, int units, int weight, States currentState, String currentOwner, int[] transferHistory) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.origin = origin;
        this.destination = destination;
        this.units = units;
        this.weight = weight;
        this.currentState = currentState;
        this.currentOwner = currentOwner;
        this.transferHistory = transferHistory;
    }
}
