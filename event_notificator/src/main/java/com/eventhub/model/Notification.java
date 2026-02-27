package com.eventhub.model;

import com.eventhub.enums.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Notification(
        Long id,

        LocalDateTime createdAt,

        Boolean isRead,

        Long eventId,

        Long ownerId,

        String oldName,

        String newName,

        Integer oldMaxPlaces,

        Integer newMaxPlaces,

        LocalDateTime oldDate,

        LocalDateTime newDate,

        BigDecimal oldCost,

        BigDecimal newCost,

        Integer oldDuration,

        Integer newDuration,

        Long oldLocationId,

        Long newLocationId,

        EventStatus oldStatus,

        EventStatus newStatus
        ) {
}
