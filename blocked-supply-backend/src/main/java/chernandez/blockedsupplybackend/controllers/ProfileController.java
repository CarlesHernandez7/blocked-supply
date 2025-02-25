package chernandez.blockedsupplybackend.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final Environment environment;

    @Value("${contract.address.file:Not Configured}")
    private String contractAddressFile;

    public ProfileController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping()
    public List<String> getActiveProfiles() {
        return Arrays.asList(environment.getActiveProfiles());
    }

    @GetMapping("/contract-file")
    public Map<String, String> getContractAddressFile() {
        Map<String, String> response = new HashMap<>();
        response.put("contractAddressFile", contractAddressFile);
        return response;
    }
}

