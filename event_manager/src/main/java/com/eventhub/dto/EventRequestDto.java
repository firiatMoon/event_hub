package com.eventhub.dto;

import com.eventhub.enums.EventStatus;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

public record EventRequestDto(
        @NotBlank(message = "The field must contain a name.")
        String name,

        @Positive(message = "{validation.be.positive}")
        @NotNull(message = "{validation.event.max.places}")
        Integer maxPlaces,

        @NotBlank(message = "{validation.event.date}")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$",
                message = "{validation.event.incorrect.date}")
        String date, //Формат "YYYY-MM-DDThh:mm:ss"

        @Min(value = 0, message = "{validation.min.cost}")
        @NotNull(message = "{validation.event.cost.required}")
        BigDecimal cost,

        @Min(value = 30, message = "{validation.min.duration}")
        @Positive(message = "{validation.be.positive}")
        @NotNull(message = "{validation.event.duration.required}")
        Integer duration,

        @NotNull
        Long locationId,

        EventStatus status
) {
}
