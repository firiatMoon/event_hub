package com.eventhub.controllers;

import com.eventhub.dto.NotificationDTO;
import com.eventhub.services.NotificationsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationsService notificationService;

    public NotificationController(NotificationsService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "Получить все непрочитанные нотификации пользователя")
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getNotifications() {
        List<NotificationDTO> list = notificationService.findAllNotReadNotification();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(list);
    }

    @Operation(summary = "Пометить нотификации, как прочитанные")
    @PostMapping
    public ResponseEntity<Void> postNotifications(@RequestBody List<Long> notificationIds) {
        notificationService.notificationRead(notificationIds);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
