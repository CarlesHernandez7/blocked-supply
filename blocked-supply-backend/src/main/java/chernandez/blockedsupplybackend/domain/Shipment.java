package chernandez.blockedsupplybackend.domain;

import lombok.Data;

@Data
public class Shipment {

    public enum State {
        CREATED, IN_TRANSIT, STORED, DELIVERED;

        public static State fromInt(int value) {
            return values()[value];
        }
    }

    private int id;
    private String name;
    private String description;
    private String origin;
    private String destination;
    private int units;
    private int weight;
    private State currentState;
    private String currentOwner;

    public Shipment(int id, String name, String description, String origin, String destination, int units, int weight, State currentState, String currentOwner) {
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
