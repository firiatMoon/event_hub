package com.eventhub.event_management.services.converter;

import com.eventhub.event_management.dto.EventDTO;
import com.eventhub.event_management.dto.EventRequestDto;
import com.eventhub.event_management.entities.EventEntity;
import com.eventhub.event_management.enums.EventStatus;
import com.eventhub.event_management.vo.Event;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EventEntityMapper {

    public EventEntity toEventEntity(Event event) {
        return new EventEntity(
                event.id(),
                event.name(),
                event.ownerId(),
                event.occupiedPlaces(),
                event.maxPlaces(),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId(),
                event.status()
        );
    }

    public Event toEvent(EventEntity eventEntity) {
        return new Event(
                eventEntity.getId(),
                eventEntity.getName(),
                eventEntity.getOwnerId(),
                eventEntity.getOccupiedPlaces(),
                eventEntity.getMaxPlaces(),
                eventEntity.getDate(),
                eventEntity.getCost(),
                eventEntity.getDuration(),
                eventEntity.getLocationId(),
                eventEntity.getStatus()
        );
    }

    public EventDTO toEventDTO(EventEntity eventEntity) {
        return new EventDTO(
                eventEntity.getId(),
                eventEntity.getName(),
                eventEntity.getOwnerId(),
                eventEntity.getOccupiedPlaces(),
                eventEntity.getMaxPlaces(),
                eventEntity.getDate(),
                eventEntity.getCost(),
                eventEntity.getDuration(),
                eventEntity.getLocationId(),
                eventEntity.getStatus()
        );
    }

    public EventEntity toCreateEventEntityFromRequest(EventRequestDto eventRequestDto, Long userId, Long locationId) {
        return new EventEntity(
                null,
                eventRequestDto.name(),
                userId,
                0,
                eventRequestDto.maxPlaces(),
                stringToLocalDateTime(eventRequestDto.date()),
                eventRequestDto.cost(),
                eventRequestDto.duration(),
                locationId,
                EventStatus.WAIT_START
        );
    }

    public EventEntity toUpdateEventEntityFromRequest(EventRequestDto eventRequestDto, Event event) {
        return new EventEntity(
                event.id(),
                eventRequestDto.name(),
                event.ownerId(),
                event.occupiedPlaces(),
                eventRequestDto.maxPlaces(),
                stringToLocalDateTime(eventRequestDto.date()),
                eventRequestDto.cost(),
                eventRequestDto.duration(),
                eventRequestDto.locationId(),
                event.status()
        );
    }

    private LocalDateTime stringToLocalDateTime(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
