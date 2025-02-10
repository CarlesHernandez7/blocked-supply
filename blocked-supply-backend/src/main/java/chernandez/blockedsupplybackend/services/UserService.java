package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.domain.Roles;
import chernandez.blockedsupplybackend.domain.User;
import chernandez.blockedsupplybackend.domain.UserRegisterDTO;
import chernandez.blockedsupplybackend.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<User> createUser(UserRegisterDTO user) {
        checkParams(user);
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
        User newUser = new User(user.getName(), user.getEmail(), hashedPassword, user.getRoles());
        userRepository.save(newUser);
        return new ResponseEntity<>(newUser, org.springframework.http.HttpStatus.CREATED);
    }

    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, org.springframework.http.HttpStatus.OK);
    }

    public ResponseEntity<User> getUserById(Long id) {
        User user = validateAndGetUserById(id);
        if (user == null) {
            return new ResponseEntity<>(null, org.springframework.http.HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, org.springframework.http.HttpStatus.OK);
    }

    public ResponseEntity<String> deleteUser(Long id) {
        User user = validateAndGetUserById(id);
        if (user == null) {
            return new ResponseEntity<>(null, org.springframework.http.HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(id);
        return new ResponseEntity<>("User deleted", org.springframework.http.HttpStatus.OK);
    }

    private User validateAndGetUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        } else if (id < 0) {
            throw new IllegalArgumentException("Id cannot be negative");
        } else {
            return userRepository.findById(id).orElse(null);
        }
    }

    private static void checkParams(UserRegisterDTO user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (!user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email must contain '@'");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }else if (user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        } else if (!user.getPassword().matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        } else if (!user.getPassword().matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter");
        } else if (!user.getPassword().matches(".*[0-9].*")) {
            throw new IllegalArgumentException("Password must contain at least one number");
        }

        for (String role : user.getRoles()) {
            if (role == null || role.isEmpty()) {
                throw new IllegalArgumentException("Role cannot be empty");
            } else {
                try {
                    Roles.valueOf(role);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid role: " + role);
                }
            }
        }
    }
}
