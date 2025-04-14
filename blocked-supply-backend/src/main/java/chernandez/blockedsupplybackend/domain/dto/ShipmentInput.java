package chernandez.blockedsupplybackend.domain.dto;

import lombok.Data;

@Data
public class ShipmentInput {
    private String name;
    private String description;
    private String origin;
    private String destination;
    private String deliveryDate;
    private int units;
    private int weight;

    public ShipmentInput() {
    }

    public ShipmentInput(String name, String description, String origin, String destination, String deliveryDate, int units, int weight) {
        this.name = name;
        this.description = description;
        this.origin = origin;
        this.destination = destination;
        this.deliveryDate = deliveryDate;
        this.units = units;
        this.weight = weight;
    }
}
