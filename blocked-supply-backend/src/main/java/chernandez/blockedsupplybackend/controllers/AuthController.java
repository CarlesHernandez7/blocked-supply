package chernandez.blockedsupplybackend.controllers;

import chernandez.blockedsupplybackend.domain.dto.auth.LoginRequest;
import chernandez.blockedsupplybackend.domain.dto.auth.RegisterRequest;
import chernandez.blockedsupplybackend.domain.dto.auth.TokenResponse;
import chernandez.blockedsupplybackend.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody final RegisterRequest request) throws Exception {
        return authService.register(request);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponse> login(@RequestBody final LoginRequest request) {
        final TokenResponse token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/auth/refresh")
    public TokenResponse refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader) {
        return authService.refreshToken(authHeader);
    }

    @GetMapping("/api/user")
    public ResponseEntity<?> getUser() throws Exception {
        return authService.getUser();
    }
}
