package com.eventhub.event_management.controllers;


import com.eventhub.event_management.dto.EventDTO;
import com.eventhub.event_management.dto.EventRequestDto;
import com.eventhub.event_management.dto.EventSearchRequestDto;
import com.eventhub.event_management.services.EventService;
import com.eventhub.event_management.services.converter.EventDTOConverted;
import com.eventhub.event_management.vo.Event;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final EventDTOConverted eventDTOConverted;

    public EventController(EventService eventService, EventDTOConverted eventDTOConverted) {
        this.eventService = eventService;
        this.eventDTOConverted = eventDTOConverted;
    }

    @Operation(summary = "Создание нового мероприятия")
    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventRequestDto eventRequestDto,
                                                HttpServletRequest request) {
        Event event = eventService.createEvent(request, eventRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventDTOConverted.toEventDTO(event));
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
                .body(eventDTOConverted.toEventDTO(event));
    }

    @Operation(summary = "Обновление мероприятия")
    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable("id") Long id,
                                                @RequestBody EventRequestDto eventRequestDto,
                                                HttpServletRequest request) {
        Event event = eventService.updateEvent(request, id, eventRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventDTOConverted.toEventDTO(event));
    }

    @Operation(summary = "Поиск мероприятий по фильтру")
    @GetMapping("/search")
    public ResponseEntity<List<EventDTO>> searchEvents(@RequestBody @Valid EventSearchRequestDto eventSearchRequestDto) {
        List<EventDTO> eventDTOList = eventService.getAllEvents(eventSearchRequestDto)
                .stream()
                .map(eventDTOConverted::toEventDTO)
                .toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventDTOList);
    }

    @Operation(summary = "Все мероприятия созданные пользователем, который выполняет запрос")
    @GetMapping("/my")
    public ResponseEntity<List<EventDTO>> getMyEvents(HttpServletRequest request) {
        List<EventDTO> eventDTOList = eventService.getMyEvents(request).stream()
                .map(eventDTOConverted::toEventDTO)
                .toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventDTOList);
    }

    @Operation(summary = "Регистрация пользователей на мероприятие поь ID")
    @PostMapping("/registrations/{id}")
    public ResponseEntity<Void> registrationEvent(@PathVariable("id") Long id, HttpServletRequest request) {
        eventService.registrationEvent(request, id);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Operation(summary = "Отмена регистрации на мероприятие")
    @DeleteMapping("/registrations/{id}")
    //Необходимо учесть статус мероприятия. Нельзя отменить регистрацию, если мероприятие
    // уже началось или закончилось.
    public ResponseEntity<Void> deleteRegistrationEvent(@PathVariable("id") Long id, HttpServletRequest request) {
        eventService.deleteRegistrationEvent(request, id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}

//@RequestParam(value = "name", required = false) String name,
//@RequestParam(value = "placesMin", required = false) Integer placesMin,
//@RequestParam(value = "placesMax", required = false) Integer placesMax,
//@RequestParam(value = "dateStartAfter", required = false) LocalDateTime dateStartAfter,
//@RequestParam(value = "dateStartBefore", required = false) LocalDateTime dateStartBefore,
//@RequestParam(value = "costMin", required = false) BigDecimal costMin,
//@RequestParam(value = "costMax", required = false) BigDecimal costMax,
//@RequestParam(value = "durationMin", required = false) Integer durationMin,
//@RequestParam(value = "durationMax", required = false) Integer durationMax,
//@RequestParam(value = "locationId", required = false) Integer locationId,
//@RequestParam(value = "eventStatus", required = false) EventStatus eventStatus
