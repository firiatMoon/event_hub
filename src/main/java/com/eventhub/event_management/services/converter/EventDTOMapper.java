package com.eventhub.event_management.services.converter;

import com.eventhub.event_management.dto.EventDTO;
import com.eventhub.event_management.vo.Event;
import org.springframework.stereotype.Component;

@Component
public class EventDTOMapper {

    public EventDTO toEventDTO (Event event){
        return new EventDTO(
                event.id(),
                event.name(),
                event.ownerId(),
                event.maxPlaces(),
                event.occupiedPlaces(),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId(),
                event.status()
        );
    }

    public Event toEvent (EventDTO eventDTO){
        return new Event(
                eventDTO.id(), eventDTO.name(),
                eventDTO.ownerId(),
                eventDTO.occupiedPlaces(),
                eventDTO.maxPlaces(),
                eventDTO.date(),
                eventDTO.cost(),
                eventDTO.duration(),
                eventDTO.locationId(),
                eventDTO.status()
        );
    }

}
