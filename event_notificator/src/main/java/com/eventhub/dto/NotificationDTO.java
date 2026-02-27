package com.eventhub.dto;

import com.eventhub.enums.EventStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record NotificationDTO(
        Long eventId,

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
