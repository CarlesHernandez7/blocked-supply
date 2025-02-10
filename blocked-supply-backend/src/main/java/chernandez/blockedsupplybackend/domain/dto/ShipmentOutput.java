package chernandez.blockedsupplybackend.domain.dto;

import lombok.Data;

import java.math.BigInteger;

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

    public enum State {
        CREATED, IN_TRANSIT, STORED, DELIVERED;

        public static State fromInt(BigInteger value) {
            return switch (value.intValue()) {
                case 0 -> CREATED;
                case 1 -> IN_TRANSIT;
                case 2 -> STORED;
                case 3 -> DELIVERED;
                default -> throw new IllegalArgumentException("Invalid state value: " + value);
            };
        }
    }
}
