package chernandez.blockedsupplybackend.services;

import chernandez.blockedsupplybackend.domain.Token;
import chernandez.blockedsupplybackend.domain.User;
import chernandez.blockedsupplybackend.domain.dto.auth.*;
import chernandez.blockedsupplybackend.repositories.TokenRepository;
import chernandez.blockedsupplybackend.repositories.UserRepository;
import chernandez.blockedsupplybackend.utils.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${application.security.encryption.secret-key}")
    private String encryptionKey;

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse register(RegisterRequest request) {
        var user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return new TokenResponse(jwtToken, refreshToken);
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

    public ResponseEntity<?> setBlockchainCredentials(BlockchainCredentialsInput credentialsInput) throws Exception {
        User user = getUserFromJWT();
        if (credentialsInput == null || credentialsInput.address() == null || credentialsInput.key() == null) {
            return new ResponseEntity<>("Blockchain credentials cannot be null", HttpStatus.BAD_REQUEST);
        }
        if (user.getBlockchainAddress() != null && user.getBlockchainKey() != null) {
            return new ResponseEntity<>("User already has a blockchain credentials", HttpStatus.BAD_REQUEST);
        }
        if (!credentialsInput.address().startsWith("0x") || !credentialsInput.key().startsWith("0x")) {
            return new ResponseEntity<>("Invalid blockchain credentials type", HttpStatus.BAD_REQUEST);
        }

        String encryptedKey = EncryptionUtil.encrypt(this.encryptionKey, credentialsInput.key());
        user.setBlockchainKey(encryptedKey);
        user.setBlockchainAddress(credentialsInput.address());
        userRepository.save(user);
        return new ResponseEntity<>("Credentials correctly set for user " + user.getId(), HttpStatus.OK);
    }

    public ResponseEntity<?> getUser() {
        User user = getUserFromJWT();
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        UserDetails details = new UserDetails(user.getName(), user.getEmail(), user.getBlockchainAddress());
        return new ResponseEntity<>(details, HttpStatus.OK);
    }
}
