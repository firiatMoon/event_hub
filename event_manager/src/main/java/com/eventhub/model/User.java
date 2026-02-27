package com.eventhub.model;

import com.eventhub.enums.Role;

public record User(
        Long id,

        String login,

        Integer age,

        Role role
) {
}
