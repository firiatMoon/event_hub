package com.eventhub.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;

public record UserDTO(
        @Null
        Long id,

        @NotBlank(message = "{validation.user.login.required}")
        String login,

        @Positive(message = "{validation.be.positive}")
        @Min(value = 18, message = "{validation.user.age.min}")
        Integer age
) {
}
