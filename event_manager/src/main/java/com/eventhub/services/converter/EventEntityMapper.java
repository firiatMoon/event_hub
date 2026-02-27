package com.eventhub.services.converter;

import com.eventhub.dto.EventDTO;
import com.eventhub.dto.EventRequestDto;
import com.eventhub.dto.EventTelegramBotDTO;
import com.eventhub.entities.EventEntity;
import com.eventhub.enums.EventStatus;
import com.eventhub.model.Event;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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

    public void updateEntityFromRequest(EventRequestDto dto, EventEntity entity) {
        entity.setName(dto.name());
        entity.setMaxPlaces(dto.maxPlaces());
        entity.setDate(stringToLocalDateTime(dto.date()));
        entity.setCost(dto.cost());
        entity.setDuration(dto.duration());
        entity.setLocationId(dto.locationId());
        entity.setStatus(Objects.isNull(dto.status()) ? entity.getStatus() : dto.status());
    }

    public EventTelegramBotDTO toEventTelegramBotDTO(EventEntity eventEntity, String location) {
        return new EventTelegramBotDTO(
                eventEntity.getName(),
                eventEntity.getDate(),
                location,
                eventEntity.getStatus()
        );
    }

    private LocalDateTime stringToLocalDateTime(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
