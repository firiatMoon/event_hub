package com.eventhub.event_management.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SingUpRequest(
        @NotBlank(message = "The login is required. Enter please.")
        String login,

        @NotBlank(message = "The password is required. Enter please.")
        String password,

        @Positive(message = "The value must be positive.")
        @Min(value = 18, message = "Minimum age: 18.")
        @NotNull(message = "The age is required. Please enter it.")
        Integer age
) {
}
