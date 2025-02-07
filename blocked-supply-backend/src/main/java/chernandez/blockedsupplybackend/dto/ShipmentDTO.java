package chernandez.blockedsupplybackend.dto;

import lombok.Data;

@Data
public class ShipmentDTO {
    private String name;
    private String description;
    private String origin;
    private String destination;
    private int units;
    private int weight;

    public ShipmentDTO() {
    }

    public ShipmentDTO(String name, String description, String origin, String destination, int units, int weight) {
        this.name = name;
        this.description = description;
        this.origin = origin;
        this.destination = destination;
        this.units = units;
        this.weight = weight;
    }
}
