package com.eventhub.event_management.exceptions;

import java.time.LocalDateTime;

public record ErrorMessageResponse(
        String message,
        String detailedMessage,
        LocalDateTime timestamp) {

}
