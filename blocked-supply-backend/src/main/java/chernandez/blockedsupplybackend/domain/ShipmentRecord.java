package chernandez.blockedsupplybackend.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "shipment_records")
public class ShipmentRecord {

    @Id
    private Long shipmentId;
    private String ownerAddress;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private State status;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "shipment_participants", joinColumns = @JoinColumn(name = "shipment_id"))
    @Column(name = "participant_id")
    private List<Long> participants = new ArrayList<>();

    public ShipmentRecord() {}

    public ShipmentRecord(Long shipmentId, String ownerAddress, State status, Long owner) {
        this.shipmentId = shipmentId;
        this.ownerAddress = ownerAddress;
        this.createdAt = LocalDateTime.now();
        this.status = status;
        addParticipant(owner);
    }

    public void addParticipant(Long participantId) {
        if (this.participants == null) {
            this.participants = new ArrayList<>();
        }
        this.participants.add(participantId);
    }
}
