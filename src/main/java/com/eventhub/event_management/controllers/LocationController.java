package com.eventhub.event_management.controllers;

import com.eventhub.event_management.dto.LocationDTO;
import com.eventhub.event_management.services.converter.LocationDTOConverter;
import com.eventhub.event_management.services.LocationService;
import com.eventhub.event_management.vo.Location;
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
    private final LocationDTOConverter locationDTOConverter;

    public LocationController(LocationService locationService, LocationDTOConverter locationDTOConverter) {
        this.locationService = locationService;
        this.locationDTOConverter = locationDTOConverter;
    }

    @Operation(summary = "Получение списка локаций")
    @GetMapping
    public ResponseEntity<List<LocationDTO>> getLocations(){
        List<LocationDTO> locationDTOS = locationService.getAllLocation().stream()
                .map(locationDTOConverter::toLocationDTO)
                .toList();
        return ResponseEntity.
                status(HttpStatus.OK)
                .body(locationDTOS);
    }

    @Operation(summary = "Создание локации")
    @PostMapping()
    public ResponseEntity<LocationDTO> createLocation(@RequestBody @Valid LocationDTO locationDTO) {
        Location createdLocation = locationService.createLocation(locationDTOConverter.toLocation(locationDTO));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(locationDTOConverter.toLocationDTO(createdLocation));
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
        Location location = locationService.getLocationById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(locationDTOConverter.toLocationDTO(location));
    }

    @Operation(summary = "Обновление локации")
    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> updateLocation(@PathVariable("id") Long id,
                                                         @RequestBody @Valid LocationDTO locationDTO) {
       Location updatedLocation = locationService.updateLocation(id, locationDTOConverter.toLocation(locationDTO));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(locationDTOConverter.toLocationDTO(updatedLocation));
    }
}
