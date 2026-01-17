package com.eventhub.event_management.controllers;

import com.eventhub.event_management.AbstractTest;
import com.eventhub.event_management.dto.LocationDTO;
import com.eventhub.event_management.enums.Role;
import com.eventhub.event_management.repositories.LocationRepository;
import com.eventhub.event_management.services.LocationService;
import com.eventhub.event_management.services.converter.LocationDTOMapper;
import com.eventhub.event_management.vo.Location;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LocationControllerTest extends AbstractTest {
    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationDTOMapper locationDTOMapper;

    @Test
    void shouldSuccessCreateLocation() throws Exception {
        LocationDTO location = new LocationDTO(
                null,
                "Game",
                "Kazan",
                34,
                null
        );

        String locationJson = mapper.writeValueAsString(location);

        String createdLocationJson = mockMvc.perform(post("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(locationJson)
                        .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(Role.ADMIN))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Location createdLocation = mapper.readValue(createdLocationJson, Location.class);
        Assertions.assertNotNull(createdLocation.id());
        Assertions.assertEquals(location.name(), createdLocation.name());
        Assertions.assertTrue(locationRepository.existsById(createdLocation.id()));
    }

    @Test
    void shouldDeleteLocation() throws Exception {
        LocationDTO locationDTO = new LocationDTO(
                null,
                "Game",
                "Kazan",
                34,
                null
        );

        Location location = locationDTOMapper.toLocation(locationDTO);

        locationDTO = locationService.createLocation(location);
        Assertions.assertNotNull(locationDTO.id());

        mockMvc.perform(delete("/locations/{id}", locationDTO.id())
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(Role.ADMIN)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnForbiddenWhenUserTriedToDeleteLocation() throws Exception {
        LocationDTO locationDTO = new LocationDTO(
                null,
                "Game",
                "Kazan",
                34,
                null
        );

        Location location = locationDTOMapper.toLocation(locationDTO);

        locationDTO = locationService.createLocation(location);
        Assertions.assertNotNull(locationDTO.id());

        mockMvc.perform(delete("/locations/{id}", locationDTO.id())
                        .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(Role.USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldSuccessSearchUserById() throws Exception {
        LocationDTO locationDTO = new LocationDTO(
                null,
                "Game",
                "Kazan",
                34,
                null
        );

        Location location = locationDTOMapper.toLocation(locationDTO);

        locationDTO = locationService.createLocation(location);
        String foundLocationJson = mockMvc.perform(get("/locations/{id}", locationDTO.id())
                        .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(Role.USER)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Location foundLocation = mapper.readValue(foundLocationJson, Location.class);
        org.assertj.core.api.Assertions.assertThat(locationDTO)
                .usingRecursiveComparison()
                .isEqualTo(foundLocation);
    }

    @Test
    void shouldReturnNotFoundLocation() throws Exception {
        mockMvc.perform(get("/locations/{id}", Long.MAX_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(Role.USER)))
                .andExpect(status().isNotFound());
    }
}