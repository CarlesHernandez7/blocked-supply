package chernandez.blockedsupplybackend.config.exceptions;

import chernandez.blockedsupplybackend.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ShipmentNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleShipmentNotFound(ShipmentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO("Shipment not found: "+e.getMessage(), e.getCause().getMessage()));
    }

    @ExceptionHandler(BlockchainException.class)
    public ResponseEntity<ErrorResponseDTO> handleBlockchainError(BlockchainException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDTO("Blockchain Error: "+e.getMessage(), e.getCause().getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralError(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDTO("Internal Server Error", e.getMessage()));
    }
}
