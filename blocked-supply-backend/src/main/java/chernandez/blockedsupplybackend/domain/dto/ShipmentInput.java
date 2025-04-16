package chernandez.blockedsupplybackend.domain.dto;

import lombok.Data;

@Data
public class ShipmentInput {
    private String productName;
    private String description;
    private String origin;
    private String destination;
    private String deliveryDate;
    private int units;
    private int weight;
    private String from;

    public ShipmentInput(String productName, String description, String origin, String destination, String deliveryDate, int units, int weight) {
        this.productName = productName;
        this.description = description;
        this.origin = origin;
        this.destination = destination;
        this.deliveryDate = deliveryDate;
        this.units = units;
        this.weight = weight;
    }
}
