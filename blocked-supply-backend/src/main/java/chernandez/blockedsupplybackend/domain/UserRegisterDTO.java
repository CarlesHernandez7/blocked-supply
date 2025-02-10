package chernandez.blockedsupplybackend.domain;

import lombok.Data;

import java.util.List;

@Data
public class UserRegisterDTO {

    private String name;
    private String email;
    private String password;
    private List<String> roles;

    public UserRegisterDTO() {
    }

    public UserRegisterDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRoles();
    }
}
