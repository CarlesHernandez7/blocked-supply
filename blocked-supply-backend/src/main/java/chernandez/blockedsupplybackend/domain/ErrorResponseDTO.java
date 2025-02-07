package chernandez.blockedsupplybackend.domain;

import lombok.Data;

@Data
public class ErrorResponseDTO {

    private String error;
    private String details;

    public ErrorResponseDTO() {
    }

    public ErrorResponseDTO(String error, String details) {
        this.error = error;
        this.details = details;
    }
}
