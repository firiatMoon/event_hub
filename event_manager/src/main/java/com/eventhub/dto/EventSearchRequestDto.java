package com.eventhub.dto;

import com.eventhub.enums.EventStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventSearchRequestDto(

        String name,

        @Positive(message = "The value must be positive.")
        Integer placesMin,

        @Positive(message = "The value must be positive.")
        Integer placesMax,

        LocalDateTime dateStartAfter,

        LocalDateTime dateStartBefore,

        @Min(value = 0, message = "Minimum cost: 0.")
        BigDecimal costMin,

        @Min(value = 0, message = "Maximum cost: 0.")
        BigDecimal costMax,

        @Min(value = 0, message = "Minimum duration: 0.")
        Integer durationMin,

        @Min(value = 0, message = "Maximum duration: 0.")
        Integer durationMax,

        @Min(value = 1, message = "Minimum value: 1.")
        Long locationId,

        EventStatus eventStatus
) {
}
