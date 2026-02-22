package com.eventhub.dto;

import com.eventhub.enums.EventStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EventTelegramBotDTO(

        @NotBlank(message = "The name of the event is required. Please enter it.")
        String name,

        @NotNull(message = "The date of the event is required. Please enter it.")
        LocalDateTime date,

        @NotNull
        String location,

        @NotNull
        EventStatus status
) {
}
