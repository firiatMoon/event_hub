package com.eventhub.dto;

import com.eventhub.enums.EventStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventSearchRequestDto(

        String name,

        @Positive(message = "{validation.be.positive}")
        Integer placesMin,

        @Positive(message = "{validation.be.positive}")
        Integer placesMax,

        LocalDateTime dateStartAfter,

        LocalDateTime dateStartBefore,

        @Min(value = 0, message = "{validation.min.cost}")
        BigDecimal costMin,

        @Min(value = 0, message = "{validation.max.cost}")
        BigDecimal costMax,

        @Min(value = 0, message = "{validation.min.duration}")
        Integer durationMin,

        @Min(value = 0, message = "{validation.max.duration}")
        Integer durationMax,

        @Min(value = 1, message = "{validation.min.value}")
        Long locationId,

        EventStatus eventStatus
) {
}
