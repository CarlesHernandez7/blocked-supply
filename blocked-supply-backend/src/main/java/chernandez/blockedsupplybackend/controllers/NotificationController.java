package chernandez.blockedsupplybackend.controllers;

import chernandez.blockedsupplybackend.domain.dto.NotificationOutput;
import chernandez.blockedsupplybackend.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping()
    public ResponseEntity<List<NotificationOutput>> getNotifications() {
        return notificationService.getNotifications();
    }

    @PostMapping("/read/{notiId}")
    public ResponseEntity<?> readNotification(@PathVariable int notiId) {
        return notificationService.markAsRead(notiId);
    }

    @PostMapping("/read")
    public ResponseEntity<?> readAllNotifications() {
        return notificationService.markAllAsRead();
    }

}
