package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.domain.User;
import chernandez.blockedsupplybackend.dto.UserRegisterDTO;
import chernandez.blockedsupplybackend.repositories.UserRepository;
import org.springframework.stereotype.Service;
import smartContracts.web3.ShipmentManagement;

import java.util.List;

@Service
public class UserService {

    private final ShipmentManagement shipmentContract;
    private final UserRepository userRepository;

    public UserService(ShipmentManagement shipmentContract, UserRepository userRepository) {
        this.shipmentContract = shipmentContract;
        this.userRepository = userRepository;
    }

    public void registerUser(UserRegisterDTO user) {
        try {
            shipmentContract.registerUser(user.getName(), user.getRoles(), user.getEmail()).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
