package com.eventhub.event_management.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

public record EventRequestDto(
        @NotBlank(message = "The field must contain a name.")
        String name,

        @Positive(message = "The value must be positive.")
        @NotNull(message = "Please specify the maximum number of participants in the event.")
        Integer maxPlaces,

        @NotBlank(message = "The field must contain the date.")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$",
                message = "Incorrect format for date.")
        String date, //Формат "YYYY-MM-DDThh:mm:ss"

        @Min(value = 0, message = "Minimum cost: 0.")
        @Positive(message = "The value must be positive.")
        @NotNull(message = "It is required to specify the cost of the event.  If the event is free, specify zero.")
        BigDecimal cost,

        @Min(value = 30, message = "Minimum duration: 30.")
        @Positive(message = "The value must be positive.")
        @NotNull(message = "The duration of the event is required. Please enter it.")
        Integer duration,

        @NotNull
        Long locationId
) {
}
