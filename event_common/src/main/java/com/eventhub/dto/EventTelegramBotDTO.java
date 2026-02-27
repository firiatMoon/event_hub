package com.eventhub.dto;

import com.eventhub.enums.EventStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EventTelegramBotDTO(
        String name,

        LocalDateTime date,

        String location,

        EventStatus status
) {
}
