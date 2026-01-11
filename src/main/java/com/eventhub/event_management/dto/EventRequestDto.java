package com.eventhub.event_management.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record EventRequestDto(
        @NotBlank
        String name,

        @Positive
        @NotNull
        Integer maxPlaces,

        @NotBlank
        String date, //Формат "YYYY-MM-DDThh:mm:ss"

        @Min(0)
        @Positive
        @NotNull
        BigDecimal cost,

        @Min(30)
        @Positive
        @NotNull
        Integer duration,

        @NotNull
        Long locationId
) {
}
