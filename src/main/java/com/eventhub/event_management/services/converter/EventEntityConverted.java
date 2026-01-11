package com.eventhub.event_management.services.converter;

import com.eventhub.event_management.entities.EventEntity;
import com.eventhub.event_management.vo.Event;
import org.springframework.stereotype.Component;

@Component
public class EventEntityConverted {

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
}
