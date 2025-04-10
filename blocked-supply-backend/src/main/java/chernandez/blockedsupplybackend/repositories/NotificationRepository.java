package chernandez.blockedsupplybackend.repositories;

import chernandez.blockedsupplybackend.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByToUserIdAndIsReadFalseOrderByCreatedAtDesc(Long toUserId);
}
