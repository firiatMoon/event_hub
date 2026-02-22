package com.eventhub.exceptions;

public class RetryableExceptions extends RuntimeException {
    public RetryableExceptions(String message) {
        super(message);
    }
    public RetryableExceptions(Throwable cause) {
        super(cause);
    }

}
