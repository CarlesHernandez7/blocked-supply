package chernandez.blockedsupplybackend.controllers;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final Environment environment;

    public ProfileController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping()
    public List<String> getActiveProfiles() {
        return Arrays.asList(environment.getActiveProfiles());
    }
}

