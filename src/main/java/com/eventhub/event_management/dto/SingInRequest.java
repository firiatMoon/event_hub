package com.eventhub.event_management.dto;

import jakarta.validation.constraints.NotBlank;


public record SingInRequest(
        @NotBlank
        String login,

        @NotBlank
        String password
) {
}
