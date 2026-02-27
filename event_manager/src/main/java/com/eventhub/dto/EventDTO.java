package com.eventhub.dto;

import com.eventhub.enums.EventStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventDTO(

        @Null
        Long id,

        @NotBlank(message = "{validation.event.name.required}")
        String name,

        @NotNull
        Long ownerId,

        Integer occupiedPlaces,

        @NotNull(message = "{validation.event.max.places}")
        @Positive(message = "{validation.be.positive}")
        Integer maxPlaces,

        @NotNull(message = "{validation.event.date.required}")
        LocalDateTime date,

        @Min(value = 0, message = "{validation.min.cost}")
        @NotNull(message = "{validation.event.cost.required}")
        BigDecimal cost,

        @Min(value = 30, message = "{validation.min.duration}")
        @Positive(message = "{validation.be.positive}")
        @NotNull(message = "{validation.event.duration.required}")
        Integer duration,

        @NotNull
        Long locationId,

        @NotNull
        EventStatus status
) {
}
