package chernandez.blockedsupplybackend.dto;

import chernandez.blockedsupplybackend.domain.User;
import lombok.Data;

import java.util.List;

@Data
public class UserRegisterDTO {

    private String name;
    private String email;
    private List<String> roles;

    public UserRegisterDTO() {
    }

    public UserRegisterDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }
}
