package com.eventhub.event_management.dto;

import com.eventhub.event_management.enums.EventStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventSearchRequestDto(

        String name,

        @Positive
        Integer placesMin,

        @Positive
        Integer placesMax,

        LocalDateTime dateStartAfter,

        LocalDateTime dateStartBefore,

        @Min(0)
        BigDecimal costMin,

        @Min(0)
        BigDecimal costMax,

        @Min(0)
        Integer durationMin,

        @Min(0)
        Integer durationMax,

        @Min(1)
        Integer locationId,

        EventStatus eventStatus
) {
}
