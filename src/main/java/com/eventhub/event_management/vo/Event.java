package com.eventhub.event_management.vo;

import com.eventhub.event_management.enums.EventStatus;

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
