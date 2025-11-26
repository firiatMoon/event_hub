package com.eventhub.event_management.dto;

import jakarta.validation.constraints.*;

public record LocationDTO(
        @Null
        Long id,

        @NotBlank
        String name,

        @NotBlank(message = "The address is required. Enter please.")
        String address,

        @NotNull(message = "The capacity is required. Enter please.")
        @Min(value = 10, message = "Minimum seats: 10.")
        @Positive(message = "The value must be positive.")
        Integer capacity,

        @Null
        String description
) {
}
