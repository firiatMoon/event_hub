package com.eventhub.event_management.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenDTO(
        @NotBlank
        String refreshToken
) {
}
