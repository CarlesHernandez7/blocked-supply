package chernandez.blockedsupplybackend.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "shipment_records")
public class ShipmentRecord {

    @Id
    private Long shipmentId;

    @Column(unique = true)
    private String sku;

    private String ownerAddress;
    private long ownerId;

    private LocalDateTime createdAt;
    private LocalDateTime deliveryDate;
    private LocalDateTime deliveredAt;

    @Enumerated(EnumType.STRING)
    private State state;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "shipment_participants", joinColumns = @JoinColumn(name = "shipment_id"))
    @Column(name = "participant_id")
    private List<Long> participants = new ArrayList<>();

    public ShipmentRecord() {
    }

    public ShipmentRecord(Long shipmentId, String ownerAddress, LocalDateTime deliveryDate, State state, Long owner) {
        this.shipmentId = shipmentId;
        this.ownerAddress = ownerAddress;
        this.createdAt = LocalDateTime.now();
        this.deliveryDate = deliveryDate;
        this.deliveredAt = null;
        this.state = state;
        generateSku();
        addParticipant(owner);
    }

    private void generateSku() {
        this.sku = "SKU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public void addParticipant(Long participantId) {
        this.ownerId = participantId;
        if (this.participants == null) {
            this.participants = new ArrayList<>();
        }
        if (!this.participants.contains(participantId)) {
            this.participants.add(participantId);
        }
    }
}
