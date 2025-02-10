package chernandez.blockedsupplybackend.repositories;

import chernandez.blockedsupplybackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u.password FROM User u WHERE u.id = :id")
    String findPasswordById(@Param("id") Long id);

    @Query("SELECT u.roles FROM User u WHERE u.id = :id")
    List<String> findRolesById(@Param("id") Long id);
}
