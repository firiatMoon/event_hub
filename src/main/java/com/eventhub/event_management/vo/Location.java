package com.eventhub.event_management.vo;

public record Location(
        Long id,
        String name,
        String address,
        Integer capacity,
        String description

) {
}
