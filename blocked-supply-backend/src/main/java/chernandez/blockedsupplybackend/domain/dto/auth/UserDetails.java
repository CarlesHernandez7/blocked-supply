package chernandez.blockedsupplybackend.domain.dto.auth;

public record UserDetails(
        String name,
        String email,
        String blockchainAddress
) {
}
