package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.domain.User;
import chernandez.blockedsupplybackend.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.DefaultBlockParameterName;
import smartContracts.web3.ShipmentManagement;

import java.util.Collections;
import java.util.List;

@Service
public class BlockchainEventListener {

    private final UserRepository userRepository;
    private final ShipmentManagement shipmentContract;

    public BlockchainEventListener(UserRepository userRepository, ShipmentManagement shipmentContract) {
        this.userRepository = userRepository;
        this.shipmentContract = shipmentContract;
    }

    @PostConstruct
    private void listenToEvents() {
        shipmentContract.userRegisteredEventFlowable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST)
                .subscribe(event -> {
                    String userAddress = event.userAddress;
                    String username = event.name;
                    String email = event.email;
                    List<String> roles = Collections.singletonList(event.roles.toString());

                    // Save the user in the database when the event is detected
                    User user = new User(userAddress, username, email, roles);
                    userRepository.save(user);
                });
    }
}
