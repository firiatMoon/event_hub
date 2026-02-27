package com.eventhub.services.handlers;

import com.eventhub.services.LocaleMessageService;
import com.eventhub.services.MessageSender;
import com.eventhub.services.TelegramBotService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartHandler implements CommandHandler {
    private final TelegramBotService telegramBotService;
    private final MessageSender messageSender;
    private final LocaleMessageService messageService;

    public StartHandler(TelegramBotService telegramBotService, MessageSender messageSender, LocaleMessageService messageService) {
        this.telegramBotService = telegramBotService;
        this.messageSender = messageSender;
        this.messageService = messageService;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        String lang = update.getMessage().getFrom().getLanguageCode();

        if (telegramBotService.isChatLinked(chatId)) {
            messageSender.sendMessage(chatId, messageService.getMessage("bot.welcome-returning-user", lang));
        } else {
            messageSender.sendMessage(chatId, messageService.getMessage("bot.welcome-new-user", lang));
        }
    }

    @Override
    public boolean canHandle(Update update) {
        return (update.hasMessage() && update.getMessage().hasText()
                && update.getMessage().getText().startsWith("/start"));
    }
}
