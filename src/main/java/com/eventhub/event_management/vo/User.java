package com.eventhub.event_management.vo;

import com.eventhub.event_management.enums.Role;

public record User(
        Long id,
        String login,
        String password,
        Integer age,
        Role role
) {
}
