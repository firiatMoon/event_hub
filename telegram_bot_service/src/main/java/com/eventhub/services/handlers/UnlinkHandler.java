package com.eventhub.services.handlers;

import com.eventhub.constants.TelegramBotConstant;
import com.eventhub.services.MessageSender;
import com.eventhub.services.TelegramBotService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UnlinkHandler implements CommandHandler{
    private static final Logger log = LoggerFactory.getLogger(UnlinkHandler.class);

    private final TelegramBotService telegramBotService;
    private final MessageSender messageSender;

    public UnlinkHandler(TelegramBotService telegramBotService, MessageSender messageSender) {
        this.telegramBotService = telegramBotService;
        this.messageSender = messageSender;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        if (!telegramBotService.isChatLinked(chatId)) {
            messageSender.sendMessage(chatId, TelegramBotConstant.AUTH_REQUIRED);
            return;
        }
        try {
            telegramBotService.deleteTelegramUser(chatId);
            messageSender.sendMessage(chatId, TelegramBotConstant.DELETE_ACCOUNT);
        } catch (EntityNotFoundException ex) {
            log.warn("Attempt to delete a non-existent user: {}", chatId);
            messageSender.sendMessage(chatId, TelegramBotConstant.NOT_FOUND_TELEGRAM_USER);
        }
    }

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage() && update.getMessage().hasText()
                && update.getMessage().getText().startsWith("/inlink");
    }
}
