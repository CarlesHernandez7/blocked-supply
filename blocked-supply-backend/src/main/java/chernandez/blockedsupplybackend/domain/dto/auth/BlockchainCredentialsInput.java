package chernandez.blockedsupplybackend.domain.dto.auth;

public record BlockchainCredentialsInput(
        String address,
        String key
) {
}
