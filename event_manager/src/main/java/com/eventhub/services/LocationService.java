package com.eventhub.services;

import com.eventhub.dto.LocationDTO;
import com.eventhub.entities.LocationEntity;
import com.eventhub.repositories.LocationRepository;
import com.eventhub.services.converter.LocationEntityMapper;
import com.eventhub.model.Location;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private static final Logger log = LoggerFactory.getLogger(LocationService.class);

    private final LocationRepository locationRepository;
    private final LocationEntityMapper locationEntityMapper;
    private final LocaleMessageService messageService;

    public LocationService(LocationRepository locationRepository, LocationEntityMapper locationEntityMapper, LocaleMessageService messageService) {
        this.locationRepository = locationRepository;
        this.locationEntityMapper = locationEntityMapper;
        this.messageService = messageService;
    }

    public List<LocationDTO> getAllLocation() {
        return locationRepository.findAll().stream().map(locationEntityMapper::toLocationDTO).toList();
    }

    public LocationDTO createLocation(Location location) {
        LocationEntity createdLocation = new LocationEntity();

        createdLocation.setName(location.name());
        createdLocation.setAddress(location.address());
        createdLocation.setCapacity(location.capacity());
        createdLocation.setDescription(location.description());
        locationRepository.save(createdLocation);
        return locationEntityMapper.toLocationDTO(createdLocation);
    }

    public void deleteLocation(Long id) {
        if(!locationRepository.existsById(id)) {
            log.error("The location with id {} does not exist", id);
            throw new EntityNotFoundException(messageService.getMessage("err.location.not-found"));
        }
        locationRepository.deleteById(id);
    }

    public Location getLocationById(Long id) {
        LocationEntity foundlocationEntity = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageService.getMessage(
                        "err.location.not-found-by-id", id)));
        return locationEntityMapper.toLocation(foundlocationEntity);
    }

    public LocationDTO updateLocation(Long id, Location location) {
        if(!locationRepository.existsById(id)) {
            log.error("The location with id {} does not exist", id);
            throw new EntityNotFoundException(messageService.getMessage("err.location.not-found-by-id", id));
        }

        LocationEntity updatedLocation = locationEntityMapper.toLocationEntity(location);
        updatedLocation.setId(id);

        locationRepository.save(updatedLocation);
        return locationEntityMapper.toLocationDTO(updatedLocation);
    }
}
