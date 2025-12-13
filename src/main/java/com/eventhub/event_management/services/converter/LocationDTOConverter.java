package com.eventhub.event_management.services.converter;

import com.eventhub.event_management.dto.LocationDTO;
import com.eventhub.event_management.vo.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationDTOConverter {

    public LocationDTO toLocationDTO(Location location) {
        return new LocationDTO(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }

    public Location toLocation(LocationDTO locationDTO) {
        return new Location(
                locationDTO.id(),
                locationDTO.name(),
                locationDTO.address(),
                locationDTO.capacity(),
                locationDTO.description()
        );
    }


}
