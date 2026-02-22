package com.eventhub.services.handlers;

import com.eventhub.constants.TelegramBotConstant;
import com.eventhub.services.MessageSender;
import com.eventhub.services.TelegramBotService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartHandler implements CommandHandler {
    private final TelegramBotService telegramBotService;
    private final MessageSender messageSender;

    public StartHandler(TelegramBotService telegramBotService, MessageSender messageSender) {
        this.telegramBotService = telegramBotService;
        this.messageSender = messageSender;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        if (telegramBotService.isChatLinked(chatId)) {
            messageSender.sendMessage(chatId, TelegramBotConstant.WELCOME_RETURNING_USER);
        } else {
            messageSender.sendMessage(chatId, TelegramBotConstant.WELCOME_NEW_USER);
        }
    }

    @Override
    public boolean canHandle(Update update) {
        return (update.hasMessage() && update.getMessage().hasText()
                && update.getMessage().getText().startsWith("/start"));
    }
}
