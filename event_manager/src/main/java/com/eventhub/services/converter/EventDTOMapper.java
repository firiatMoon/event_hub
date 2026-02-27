package com.eventhub.services.converter;

import com.eventhub.dto.EventDTO;
import com.eventhub.model.Event;
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
}
