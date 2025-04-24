package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.domain.Token;
import chernandez.blockedsupplybackend.domain.User;
import chernandez.blockedsupplybackend.domain.dto.auth.LoginRequest;
import chernandez.blockedsupplybackend.domain.dto.auth.RegisterRequest;
import chernandez.blockedsupplybackend.domain.dto.auth.TokenResponse;
import chernandez.blockedsupplybackend.domain.dto.auth.UserDetails;
import chernandez.blockedsupplybackend.repositories.TokenRepository;
import chernandez.blockedsupplybackend.repositories.UserRepository;
import chernandez.blockedsupplybackend.utils.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${application.broker.address}")
    private String brokerBaseUrl;
    @Value("${application.security.encryption.secret-key}")
    private String encryptionKey;

    public ResponseEntity<?> register(RegisterRequest request) throws Exception {
        ResponseEntity<?> validationResult = checkRegisterInput(request);
        if (validationResult != null) {
            return validationResult;
        }

        String encryptedAddress = getAvailableBlockchainAddressEncrypted();

        var user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .blockchainAddress(encryptedAddress)
                .build();

        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);

        return ResponseEntity.ok(new TokenResponse(jwtToken, refreshToken));
    }

    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        var user = userRepository.findByEmail(request.email()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return new TokenResponse(jwtToken, refreshToken);
    }

    public TokenResponse refreshToken(final String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Bearer token");
        }

        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail == null) {
            throw new IllegalArgumentException("Invalid Refresh token");
        }

        final User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException(userEmail));

        final String accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        return new TokenResponse(accessToken, refreshToken);
    }

    public User getUserFromJWT() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(Token.TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        final List<Token> validUserTokens = tokenRepository.findAllValidIsFalseOrRevokedIsFalseByUserId(user.getId());
        if (!validUserTokens.isEmpty()) {
            for (final Token token : validUserTokens) {
                token.setExpired(true);
                token.setRevoked(true);
            }
            tokenRepository.saveAll(validUserTokens);
        }
    }

    public ResponseEntity<?> getUser() throws Exception {
        User user = getUserFromJWT();
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        UserDetails details = new UserDetails(user.getName(), user.getEmail(), EncryptionUtil.decrypt(encryptionKey, user.getBlockchainAddress()));
        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    private ResponseEntity<?> checkRegisterInput(RegisterRequest request) {
        if (request.email() == null || request.password() == null || request.name() == null) {
            return ResponseEntity.badRequest().body("Email, password and name cannot be null");
        }
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        if (request.password().length() < 8) {
            return ResponseEntity.badRequest().body("Password has to be at least 8 characters long");
        }
        if (!request.password().matches(".*[a-zA-Z].*") || !request.password().matches(".*[0-9].*")) {
            return ResponseEntity.badRequest().body("Password has to contain at least one letter and one number");
        }
        if (request.name().length() < 3) {
            return ResponseEntity.badRequest().body("Name has to be at least 3 characters long");
        }
        if (request.email().length() < 5 || !request.email().contains("@")) {
            return ResponseEntity.badRequest().body("Email has to be at least 5 characters long and contain @");
        }
        return null;
    }

    private String getAvailableBlockchainAddressEncrypted() throws Exception {
        ResponseEntity<Map<String, List<String>>> response = restTemplate.exchange(
                brokerBaseUrl + "/api/accounts",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        List<String> accounts = response.getBody().get("accounts");

        List<String> usedAddresses = userRepository.findAll()
                .stream()
                .map(user -> {
                    try {
                        return EncryptionUtil.decrypt(encryptionKey, user.getBlockchainAddress());
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to decrypt blockchain address", e);
                    }
                })
                .toList();

        return EncryptionUtil.encrypt(encryptionKey, accounts.stream()
                .filter(addr -> !usedAddresses.contains(addr))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No available blockchain addresses.")));
    }
}
