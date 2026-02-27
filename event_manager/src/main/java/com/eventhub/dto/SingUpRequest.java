package com.eventhub.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SingUpRequest(
        @NotBlank(message = "{validation.user.login.required}")
        String login,

        @NotBlank(message = "{validation.user.password.required}")
        String password,

        @Positive(message = "{validation.be.positive}")
        @Min(value = 18, message = "{validation.user.age.min}")
        @NotNull(message = "{validation.user.age.required}")
        Integer age
) {
}
