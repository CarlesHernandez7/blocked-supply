package chernandez.blockedsupplybackend.domain.dto.auth;

public record RegisterRequest(
        String email,
        String password,
        String name
) {
}
