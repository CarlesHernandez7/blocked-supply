package chernandez.blockedsupplybackend.domain;

import lombok.Data;

import java.util.List;

@Data
public class User {

    private String userAddress;
    private String name;
    private List<Roles> roles;
    private String email;

    public User(String userAddress, String name, List<Roles> roles, String email) {
        this.userAddress = userAddress;
        this.name = name;
        this.roles = roles;
        this.email = email;
    }
}
