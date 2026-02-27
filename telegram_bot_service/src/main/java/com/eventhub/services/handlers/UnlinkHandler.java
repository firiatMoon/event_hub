package com.eventhub.services.handlers;

import com.eventhub.services.LocaleMessageService;
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
    private final LocaleMessageService messageService;

    public UnlinkHandler(TelegramBotService telegramBotService, MessageSender messageSender, LocaleMessageService messageService) {
        this.telegramBotService = telegramBotService;
        this.messageSender = messageSender;
        this.messageService = messageService;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        String lang = update.getMessage().getFrom().getLanguageCode();

        if (!telegramBotService.isChatLinked(chatId)) {
            messageSender.sendMessage(chatId, messageService.getMessage("bot.auth-required", lang));
            return;
        }
        try {
            telegramBotService.deleteTelegramUser(chatId);
            messageSender.sendMessage(chatId, messageService.getMessage("bot.delete-account", lang));
        } catch (EntityNotFoundException ex) {
            log.warn("Attempt to delete a non-existent user: {}", chatId);
            messageSender.sendMessage(chatId, messageService.getMessage("bot.not-found-telegram-user", lang));
        }
    }

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage() && update.getMessage().hasText()
                && update.getMessage().getText().startsWith("/inlink");
    }
}
