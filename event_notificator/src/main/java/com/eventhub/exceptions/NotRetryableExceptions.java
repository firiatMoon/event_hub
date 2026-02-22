package com.eventhub.exceptions;

public class NotRetryableExceptions extends RuntimeException {
    public NotRetryableExceptions(String message) {
        super(message);
    }
    public NotRetryableExceptions(String message, Throwable cause) {
        super(message, cause);
    }

}
