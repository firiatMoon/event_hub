package com.eventhub.controllers;

import com.eventhub.dto.EventTelegramBotDTO;
import com.eventhub.services.EventService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/telegram")
public class InternalTelegramController {

    private final EventService eventService;

    public InternalTelegramController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "Получение мероприятий пользователя (внутренний метод для бота)")
    @GetMapping("/user/{userId}/events")
    public ResponseEntity<List<EventTelegramBotDTO>> getEventsForBot(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean onlyToday) {

        List<EventTelegramBotDTO> events = eventService.getEventsByUserId(userId, onlyToday);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(events);
    }
}
