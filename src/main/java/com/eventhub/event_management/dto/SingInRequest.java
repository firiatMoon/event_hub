package com.eventhub.event_management.dto;

import jakarta.validation.constraints.NotBlank;


public record SingInRequest(
        @NotBlank(message = "The login is required. Enter please.")
        String login,

        @NotBlank(message = "The password is required. Enter please.")
        String password
) {
}
