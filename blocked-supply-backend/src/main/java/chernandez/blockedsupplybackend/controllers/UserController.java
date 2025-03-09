package chernandez.blockedsupplybackend.controllers;

import chernandez.blockedsupplybackend.domain.User;
import chernandez.blockedsupplybackend.domain.dto.UserAddressInput;
import chernandez.blockedsupplybackend.domain.dto.UserRegisterDTO;
import chernandez.blockedsupplybackend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        return this.userService.createUser(userRegisterDTO);
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        return this.userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return this.userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return this.userService.deleteUser(id);
    }

    @PutMapping("/address/{id}")
    public ResponseEntity<?> setUserAddress(@PathVariable Long id, @RequestBody UserAddressInput userAddress) {
        return this.userService.setUserAddress(id, userAddress.address());
    }
}
