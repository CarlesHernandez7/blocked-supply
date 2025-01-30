package chernandez.blockedsupplybackend.repositories;

import chernandez.blockedsupplybackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
