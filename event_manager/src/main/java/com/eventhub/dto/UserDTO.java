package com.eventhub.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;

public record UserDTO(
        @Null
        Long id,

        @NotBlank
        String login,

        @Positive
        @Min(18)
        Integer age
) {
}
