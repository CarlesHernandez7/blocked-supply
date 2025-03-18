package chernandez.blockedsupplybackend.domain.dto.auth;

public record LoginRequest(
        String email,
        String password
) {
}
