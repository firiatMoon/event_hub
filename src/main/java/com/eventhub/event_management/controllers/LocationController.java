package com.eventhub.event_management.controllers;

import com.eventhub.event_management.dto.LocationDTO;
import com.eventhub.event_management.entities.Location;
import com.eventhub.event_management.services.LocationConverter;
import com.eventhub.event_management.services.LocationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

    private final LocationService locationService;
    private final LocationConverter locationConverter;

    public LocationController(LocationService locationService, LocationConverter locationConverter) {
        this.locationService = locationService;
        this.locationConverter = locationConverter;
    }

    @GetMapping
    public ResponseEntity<List<LocationDTO>> getLocations(
            @RequestParam(value = "page", defaultValue = "0")  @Min(0)  int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(0) @Max(20) int size
    ){
        Page<Location> locations = locationService.getAllLocation(PageRequest.of(page, size));
        List<LocationDTO> locationDTOS = locations.stream()
                .map(locationConverter::toLocationDTO)
                .toList();
        return ResponseEntity.
                status(HttpStatus.OK)
                .body(locationDTOS);
    }

    @PostMapping()
    public ResponseEntity<LocationDTO> createLocation(@RequestBody @Valid Location location) {
        Location createdLocation = locationService.createLocation(location);
        LocationDTO locationDTO = locationConverter.toLocationDTO(createdLocation);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(locationDTO);
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
        LocationDTO locationDTO = locationConverter.toLocationDTO(location);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(locationDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> updateLocation(@PathVariable("id") Long id,
                                                      @RequestBody @Valid Location location) {
        Location updatedLocation = locationService.updateLocation(id, location);
        LocationDTO locationDTO = locationConverter.toLocationDTO(updatedLocation);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(locationDTO);
    }
}
