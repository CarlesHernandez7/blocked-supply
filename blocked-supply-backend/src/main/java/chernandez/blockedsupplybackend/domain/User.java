package chernandez.blockedsupplybackend.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String userAddress;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    public User() {
    }

    public User(String userAddress, String name, String email, List<String> roles) {
        this.userAddress = userAddress;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

}
