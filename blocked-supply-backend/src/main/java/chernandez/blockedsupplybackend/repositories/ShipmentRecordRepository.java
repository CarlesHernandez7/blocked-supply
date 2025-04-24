package chernandez.blockedsupplybackend.repositories;

import chernandez.blockedsupplybackend.domain.ShipmentRecord;
import chernandez.blockedsupplybackend.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShipmentRecordRepository extends JpaRepository<ShipmentRecord, Long> {

    List<ShipmentRecord> findByStateNot(State status);

    List<ShipmentRecord> findByStateAndCreatedAtBetween(State status, LocalDateTime start, LocalDateTime end);

    List<ShipmentRecord> findByParticipantsContaining(Long userId);

    List<ShipmentRecord> findByOwnerId(Long ownerId);

    Optional<ShipmentRecord> findBySku(String sku);
}
