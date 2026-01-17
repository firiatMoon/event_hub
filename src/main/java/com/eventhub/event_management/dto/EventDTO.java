package com.eventhub.event_management.dto;

import com.eventhub.event_management.enums.EventStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventDTO(

        @Null
        Long id,

        @NotBlank(message = "The name of the event is required. Please enter it.")
        String name,

        @NotNull
        Long ownerId,

        Integer occupiedPlaces,

        @NotNull(message = "Please specify the maximum number of participants in the event.")
        @Positive(message = "The value must be positive.")
        Integer maxPlaces,

        @NotNull(message = "The date of the event is required. Please enter it.")
        LocalDateTime date,

        @Min(value = 0, message = "Minimum cost: 0.")
        @Positive(message = "The value must be positive.")
        @NotNull(message = "It is required to specify the cost of the event.  If the event is free, specify zero.")
        BigDecimal cost,

        @Min(value = 30, message = "Minimum duration: 30.")
        @Positive(message = "The value must be positive.")
        @NotNull(message = "The duration of the event is required. Please enter it.")
        Integer duration,

        @NotNull
        Long locationId,

        @NotNull
        EventStatus status
) {
}
