package com.eventhub.event_management.services;

import com.eventhub.event_management.dto.LocationDTO;
import com.eventhub.event_management.entities.Location;
import com.eventhub.event_management.repositories.LocationRepository;
import org.springframework.stereotype.Component;

@Component
public class LocationConverter {

    private final LocationRepository locationRepository;

    public LocationConverter(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public LocationDTO toLocationDTO(Location location) {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setName(location.getName());
        locationDTO.setAddress(location.getAddress());
        locationDTO.setCapacity(location.getCapacity());
        locationDTO.setDescription(location.getDescription());
        return locationDTO;
    }

    public Location toLocation(LocationDTO locationDTO) {
        Location location = new Location();
        location.setName(locationDTO.getName());
        location.setAddress(locationDTO.getAddress());
        location.setCapacity(locationDTO.getCapacity());
        location.setDescription(locationDTO.getDescription());
        return location;
    }


}
