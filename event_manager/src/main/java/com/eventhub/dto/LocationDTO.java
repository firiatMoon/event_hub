package com.eventhub.dto;

import jakarta.validation.constraints.*;

public record LocationDTO(
        @Null
        Long id,

        @NotBlank(message = "{validation.location.name.required}")
        String name,

        @NotBlank(message = "{validation.location.address.required}")
        String address,

        @NotNull(message = "{validation.location.capacity.required}")
        @Min(value = 10, message = "{validation.min.seat}")
        @Positive(message = "{validation.be.positive}")
        Integer capacity,

        @Null
        String description
) {
}
