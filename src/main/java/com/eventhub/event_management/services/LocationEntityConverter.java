package com.eventhub.event_management.services;

import com.eventhub.event_management.dto.LocationDTO;
import com.eventhub.event_management.entities.LocationEntity;
import com.eventhub.event_management.vo.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationEntityConverter {

    public LocationEntity toLocationEntity(Location location) {
        return new LocationEntity(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }

    public Location toLocation(LocationEntity locationEntity) {
        return new Location(
                locationEntity.getId(),
                locationEntity.getName(),
                locationEntity.getAddress(),
                locationEntity.getCapacity(),
                locationEntity.getDescription()
        );
    }


}
