package com.eventhub.model;

import com.eventhub.enums.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Event(
        Long id,

        String name,

        Long ownerId,

        Integer occupiedPlaces,

        Integer maxPlaces,

        LocalDateTime date,

        BigDecimal cost,

        Integer duration,

        Long locationId,

        EventStatus status
) {
}
