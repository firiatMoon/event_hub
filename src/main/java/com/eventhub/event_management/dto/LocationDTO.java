package com.eventhub.event_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {

    @NotBlank(message = "The name is required. Enter please.")
    private String name;

    @NotBlank(message = "The address is required. Enter please.")
    private String address;

    @NotNull(message = "The capacity is required. Enter please.")
    @Min(value = 10, message = "Minimum seats: 10.")
    @Positive(message = "The value must be positive.")
    private Integer capacity;

    @Null
    private String description;


    public @NotBlank(message = "The name is required. Enter please.") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "The name is required. Enter please.") String name) {
        this.name = name;
    }

    public @NotBlank(message = "The address is required. Enter please.") String getAddress() {
        return address;
    }

    public void setAddress(@NotBlank(message = "The address is required. Enter please.") String address) {
        this.address = address;
    }

    public @NotNull(message = "The capacity is required. Enter please.") @Min(value = 10, message = "Minimum seats: 10.") @Positive(message = "The value must be positive.") Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(@NotNull(message = "The capacity is required. Enter please.") @Min(value = 10, message = "Minimum seats: 10.") @Positive(message = "The value must be positive.") Integer capacity) {
        this.capacity = capacity;
    }

    public @Null String getDescription() {
        return description;
    }

    public void setDescription(@Null String description) {
        this.description = description;
    }
}
