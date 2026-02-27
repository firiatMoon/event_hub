package com.eventhub.services.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandHandler {
    void handle (Update update);
    boolean canHandle (Update update);
}
