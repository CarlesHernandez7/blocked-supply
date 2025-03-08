package chernandez.blockedsupplybackend.repositories;

import chernandez.blockedsupplybackend.domain.ShipmentRecord;
import chernandez.blockedsupplybackend.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShipmentRecordRepository extends JpaRepository<ShipmentRecord, Long> {

    List<ShipmentRecord> findByStatusNot(State status);

    List<ShipmentRecord> findByStatusAndCreatedAtBetween(State status, LocalDateTime start, LocalDateTime end);

    List<ShipmentRecord> findByParticipantsContaining(Long userId);
}
