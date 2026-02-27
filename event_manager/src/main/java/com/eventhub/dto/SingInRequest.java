package com.eventhub.dto;

import jakarta.validation.constraints.NotBlank;


public record SingInRequest(
        @NotBlank(message = "{validation.user.login.required}")
        String login,

        @NotBlank(message = "{validation.user.password.required}")
        String password
) {
}
