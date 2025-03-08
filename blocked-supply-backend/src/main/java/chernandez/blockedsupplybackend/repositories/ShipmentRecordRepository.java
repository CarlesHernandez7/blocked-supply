package chernandez.blockedsupplybackend.repositories;

import chernandez.blockedsupplybackend.domain.ShipmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRecordRepository extends JpaRepository<ShipmentRecord, Long> {

}
