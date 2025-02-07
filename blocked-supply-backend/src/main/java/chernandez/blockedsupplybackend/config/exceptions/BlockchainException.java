package chernandez.blockedsupplybackend.config.exceptions;

public class BlockchainException extends RuntimeException {
    public BlockchainException(String message, Throwable cause) {
        super(message, cause);
    }
}
