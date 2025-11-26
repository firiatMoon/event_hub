package com.eventhub.event_management.controllers;

import com.eventhub.event_management.dto.LocationDTO;
import com.eventhub.event_management.services.LocationDTOConverter;
import com.eventhub.event_management.services.LocationService;
import com.eventhub.event_management.vo.Location;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

    private final LocationService locationService;
    private final LocationDTOConverter locationDTOConverter;

    public LocationController(LocationService locationService, LocationDTOConverter locationDTOConverter) {
        this.locationService = locationService;
        this.locationDTOConverter = locationDTOConverter;
    }

    @GetMapping
    public ResponseEntity<List<LocationDTO>> getLocations(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){
        List<Location> locations = locationService.getAllLocation(PageRequest.of(page, size));
        List<LocationDTO> locationDTOS = locations.stream()
                .map(locationDTOConverter::toLocationDTO)
                .toList();
        return ResponseEntity.
                status(HttpStatus.OK)
                .body(locationDTOS);
    }

    @PostMapping()
    public ResponseEntity<LocationDTO> createLocation(@RequestBody @Valid LocationDTO locationDTO) {
        Location createdLocation = locationService.createLocation(locationDTOConverter.toLocation(locationDTO));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(locationDTOConverter.toLocationDTO(createdLocation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable("id") Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable("id") Long id) {
        Location location = locationService.getLocationById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(locationDTOConverter.toLocationDTO(location));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> updateLocation(@PathVariable("id") Long id,
                                                         @RequestBody @Valid LocationDTO locationDTO) {
       Location updatedLocation = locationService.updateLocation(id, locationDTOConverter.toLocation(locationDTO));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(locationDTOConverter.toLocationDTO(updatedLocation));
    }

}
