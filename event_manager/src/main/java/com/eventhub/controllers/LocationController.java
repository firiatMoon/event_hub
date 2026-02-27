package com.eventhub.controllers;

import com.eventhub.dto.LocationDTO;
import com.eventhub.services.LocationService;
import com.eventhub.services.converter.LocationDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;
    private final LocationDTOMapper locationDTOMapper;

    public LocationController(LocationService locationService, LocationDTOMapper locationDTOMapper) {
        this.locationService = locationService;
        this.locationDTOMapper = locationDTOMapper;
    }

    @Operation(summary = "Получение списка локаций")
    @GetMapping
    public ResponseEntity<List<LocationDTO>> getLocations(){
        List<LocationDTO> locations = locationService.getAllLocation();
        return ResponseEntity.
                status(HttpStatus.OK)
                .body(locations);
    }

    @Operation(summary = "Создание локации")
    @PostMapping()
    public ResponseEntity<LocationDTO> createLocation(@RequestBody @Valid LocationDTO locationDTO) {
        LocationDTO createdLocation = locationService.createLocation(locationDTOMapper.toLocation(locationDTO));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdLocation);
    }

    @Operation(summary = "Удаление локации по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable("id") Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Operation(summary = "Получение локации по id")
    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable("id") Long id) {
        LocationDTO location = locationDTOMapper.toLocationDTO(locationService.getLocationById(id));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(location);
    }

    @Operation(summary = "Обновление локации")
    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> updateLocation(@PathVariable("id") Long id,
                                                         @RequestBody @Valid LocationDTO locationDTO) {
       LocationDTO updatedLocation = locationService.updateLocation(id, locationDTOMapper.toLocation(locationDTO));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedLocation);
    }
}
