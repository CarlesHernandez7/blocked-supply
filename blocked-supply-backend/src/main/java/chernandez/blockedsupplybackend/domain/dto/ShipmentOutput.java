package chernandez.blockedsupplybackend.domain.dto;

import chernandez.blockedsupplybackend.domain.State;
import lombok.Data;

@Data
public class ShipmentOutput {
    private int id;
    private String name;
    private String description;
    private String origin;
    private String destination;
    private int units;
    private int weight;
    private State currentState;
    private String currentOwner;

    public ShipmentOutput() {
    }

    public ShipmentOutput(int id, String name, String description, String origin, String destination, int units, int weight, State currentState, String currentOwner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.origin = origin;
        this.destination = destination;
        this.units = units;
        this.weight = weight;
        this.currentState = currentState;
        this.currentOwner = currentOwner;
    }
}
