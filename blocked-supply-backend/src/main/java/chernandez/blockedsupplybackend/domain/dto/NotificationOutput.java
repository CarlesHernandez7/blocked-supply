package chernandez.blockedsupplybackend.domain.dto;

import java.time.LocalDateTime;

public record NotificationOutput(
        Long id,
        String message,
        LocalDateTime createdAt,
        boolean isRead
) {
}
