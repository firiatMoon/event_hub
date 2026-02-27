package com.eventhub.services.converter;

import com.eventhub.dto.LocationDTO;
import com.eventhub.model.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationDTOMapper {

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
