package com.eventhub.event_management.dto;

import com.eventhub.event_management.enums.EventStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventDTO(

        @Null
        Long id,

        @NotBlank
        String name,

        @NotNull
        Long ownerId,

        @NotNull
        @Positive
        Integer maxPlaces,

        @NotNull
        @Positive
        Integer occupiedPlaces,

        @NotNull
        LocalDateTime date,

        @Min(0)
        @Positive
        @NotNull
        BigDecimal cost,

        @Min(30)
        @Positive
        @NotNull
        Integer duration,

        @NotNull
        Long locationId,

        @NotNull
        EventStatus status
) {
}
