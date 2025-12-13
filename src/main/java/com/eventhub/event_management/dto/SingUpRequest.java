package com.eventhub.event_management.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SingUpRequest(
        @NotBlank
        String login,

        @NotBlank
        String password,

        @Positive
        @Min(18)
        @NotNull
        Integer age
) {
}
