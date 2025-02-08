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
    public ResponseEntity<String> createUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        this.userService.registerUser(userRegisterDTO);
        return new ResponseEntity<>("User created", org.springframework.http.HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(this.userService.getAllUsers(), org.springframework.http.HttpStatus.OK);
    }
}
