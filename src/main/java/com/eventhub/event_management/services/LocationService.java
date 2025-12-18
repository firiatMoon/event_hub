package com.eventhub.event_management.services;

import com.eventhub.event_management.entities.LocationEntity;
import com.eventhub.event_management.repositories.LocationRepository;
import com.eventhub.event_management.services.converter.LocationEntityConverter;
import com.eventhub.event_management.vo.Location;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationEntityConverter locationEntityConverter;

    public LocationService(LocationRepository locationRepository, LocationEntityConverter locationEntityConverter) {
        this.locationRepository = locationRepository;
        this.locationEntityConverter = locationEntityConverter;
    }

    public List<Location> getAllLocation() {
        return locationRepository.findAll().stream().map(locationEntityConverter::toLocation).toList();
    }

    public Location createLocation(Location location) {
        LocationEntity createdLocation = new LocationEntity();

        createdLocation.setName(location.name());
        createdLocation.setAddress(location.address());
        createdLocation.setCapacity(location.capacity());
        createdLocation.setDescription(location.description());
        locationRepository.save(createdLocation);
        return locationEntityConverter.toLocation(createdLocation);
    }

    public void deleteLocation(Long id) {
        if(!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Location not found");
        }
        locationRepository.deleteById(id);
    }

    public Location getLocationById(Long id) {
        LocationEntity foundlocationEntity = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No location found with id: " + id));
        return locationEntityConverter.toLocation(foundlocationEntity);
    }

    public Location updateLocation(Long id, Location location) {
        if(!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("No location found with id: " + id);
        }

        LocationEntity updatedLocation = locationEntityConverter.toLocationEntity(location);
        updatedLocation.setId(id);

        locationRepository.save(updatedLocation);
        return locationEntityConverter.toLocation(updatedLocation);
    }
}
