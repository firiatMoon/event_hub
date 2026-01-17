package com.eventhub.event_management.controllers;


import com.eventhub.event_management.dto.EventDTO;
import com.eventhub.event_management.dto.EventRequestDto;
import com.eventhub.event_management.dto.EventSearchRequestDto;
import com.eventhub.event_management.enums.EventStatus;
import com.eventhub.event_management.services.EventService;
import com.eventhub.event_management.services.converter.EventDTOMapper;
import com.eventhub.event_management.vo.Event;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final EventDTOMapper eventDTOMapper;

    public EventController(EventService eventService, EventDTOMapper eventDTOMapper) {
        this.eventService = eventService;
        this.eventDTOMapper = eventDTOMapper;
    }

    @Operation(summary = "Создание нового мероприятия")
    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody @Valid EventRequestDto eventRequestDto,
                                                HttpServletRequest request) {
        EventDTO event = eventService.createEvent(request, eventRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(event);
    }

    @Operation(summary = "Удаление мероприятия")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id, HttpServletRequest request) {
        eventService.deleteEvent(request, id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Operation(summary = "Получение мероприятия по ID")
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEvent(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventDTOMapper.toEventDTO(event));
    }

    @Operation(summary = "Обновление мероприятия")
    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable("id") Long id,
                                                @RequestBody @Valid EventRequestDto eventRequestDto,
                                                HttpServletRequest request) {
        EventDTO event = eventService.updateEvent(request, id, eventRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(event);
    }

    @Operation(summary = "Поиск мероприятий по фильтру")
    @GetMapping("/search")
    public ResponseEntity<List<EventDTO>> searchEvents(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "placesMin", required = false) Integer placesMin,
            @RequestParam(value = "placesMax", required = false) Integer placesMax,
            @RequestParam(value = "dateStartAfter", required = false) LocalDateTime dateStartAfter,
            @RequestParam(value = "dateStartBefore", required = false) LocalDateTime dateStartBefore,
            @RequestParam(value = "costMin", required = false) BigDecimal costMin,
            @RequestParam(value = "costMax", required = false) BigDecimal costMax,
            @RequestParam(value = "durationMin", required = false) Integer durationMin,
            @RequestParam(value = "durationMax", required = false) Integer durationMax,
            @RequestParam(value = "locationId", required = false) Long locationId,
            @RequestParam(value = "eventStatus", required = false) EventStatus eventStatus) {
        EventSearchRequestDto eventSearchRequestDto = new EventSearchRequestDto(
                name, placesMin, placesMax, dateStartAfter, dateStartBefore,
                costMin, costMax, durationMin, durationMax, locationId, eventStatus
        );
        List<EventDTO> events = eventService.getAllEvents(eventSearchRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(events);
    }

    @Operation(summary = "Все мероприятия созданные пользователем, который выполняет запрос")
    @GetMapping("/my")
    public ResponseEntity<List<EventDTO>> getMyEvents(HttpServletRequest request) {
        List<EventDTO> events = eventService.getMyEvents(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(events);
    }

    @Operation(summary = "Регистрация пользователей на мероприятие по ID")
    @PostMapping("/registrations/{id}")
    public ResponseEntity<Void> registrationEvent(@PathVariable("id") Long id, HttpServletRequest request) {
        eventService.registrationEvent(request, id);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Operation(summary = "Получение мероприятия, на которые зарегистрирован текущий пользователь")
    @GetMapping("/registrations/my")
    public ResponseEntity<List<EventDTO>> getMyRegistrationEvents(HttpServletRequest request) {
        List<EventDTO> events = eventService.getMyRegistrationEvents(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(events);
    }

    @Operation(summary = "Отмена регистрации на мероприятие")
    @DeleteMapping("/registrations/cancel/{id}")
    public ResponseEntity<Void> deleteRegistrationEvent(@PathVariable("id") Long id, HttpServletRequest request) {
        eventService.deleteRegistrationEvent(request, id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}

