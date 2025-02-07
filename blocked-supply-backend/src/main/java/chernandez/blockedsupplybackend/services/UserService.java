package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.domain.User;
import chernandez.blockedsupplybackend.dto.UserRegisterDTO;
import chernandez.blockedsupplybackend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(UserRegisterDTO user) {
        userRepository.save(new User(user.getName(), user.getEmail(), user.getRoles()));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
