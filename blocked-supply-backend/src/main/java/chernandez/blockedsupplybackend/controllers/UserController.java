package chernandez.blockedsupplybackend.controllers;

import chernandez.blockedsupplybackend.domain.User;
import chernandez.blockedsupplybackend.domain.UserRegisterDTO;
import chernandez.blockedsupplybackend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        return this.userService.registerUser(userRegisterDTO);
    }

    @GetMapping()
    public ResponseEntity<List<User>> getUsers() {
        return this.userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return this.userService.getUserById(id);
    }
}
