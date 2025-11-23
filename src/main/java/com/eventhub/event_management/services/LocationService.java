package com.eventhub.event_management.services;

import com.eventhub.event_management.entities.Location;
import com.eventhub.event_management.repositories.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

//    public List<Location> getAllLocation(Pageable pageable) {
//        return locationRepository.searchLocations(pageable);
//    }

    public Page<Location> getAllLocation(Pageable pageable) {
        return locationRepository.findAll(pageable);
    }

    public Location createLocation(@Valid Location location) {

        Location createdLocation = new Location();

        createdLocation.setName(location.getName());
        createdLocation.setAddress(location.getAddress());
        createdLocation.setCapacity(location.getCapacity());
        createdLocation.setDescription(location.getDescription());

        locationRepository.save(createdLocation);
        return createdLocation;
    }

    public void deleteLocation(Long id) {
        if(!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Location not found");
        }
        locationRepository.deleteById(id);
    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No location found with id: " + id));
    }

    public Location updateLocation(Long id, Location location) {
        if(!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("No location found with id: " + id);
        }

        Location updatedLocation = getLocationById(id);

        updatedLocation.setName(location.getName());
        updatedLocation.setAddress(location.getAddress());
        updatedLocation.setCapacity(location.getCapacity());
        updatedLocation.setDescription(location.getDescription());

        locationRepository.save(updatedLocation);
        return updatedLocation;
    }
}
