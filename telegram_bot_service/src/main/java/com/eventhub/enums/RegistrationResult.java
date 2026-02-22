package com.eventhub.enums;

public enum RegistrationResult {
    SUCCESS("Account successfully linked!"),
    ALREADY_EXISTS("Your account is already linked to this bot.");

    private final String description;

    RegistrationResult(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
