package com.eventhub.services.handlers;

import com.eventhub.enums.RegistrationResult;
import com.eventhub.services.LocaleMessageService;
import com.eventhub.services.MessageSender;
import com.eventhub.services.TelegramBotService;
import com.eventhub.services.clients.EventManagerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class VerificationHandler implements CommandHandler{
    private final static Logger log = LoggerFactory.getLogger(VerificationHandler.class);

    private final MessageSender messageSender;
    private final TelegramBotService telegramBotService;
    private final EventManagerClient eventManagerClient;
    private final LocaleMessageService messageService;

    public VerificationHandler(MessageSender messageSender, TelegramBotService telegramBotService, EventManagerClient eventManagerClient, LocaleMessageService messageService) {
        this.messageSender = messageSender;
        this.telegramBotService = telegramBotService;
        this.eventManagerClient = eventManagerClient;
        this.messageService = messageService;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        String code = update.getMessage().getText().trim();
        String lang = update.getMessage().getFrom().getLanguageCode();

        if (telegramBotService.isChatLinked(chatId)) {
            messageSender.sendMessage(chatId, messageService.getMessage("bot.welcome-returning-user", lang));
            return;
        }
        Long userId = eventManagerClient.verifyCode(code);

        RegistrationResult result = telegramBotService.saveTelegramUser(userId, chatId);
        messageSender.sendMessage(chatId, result.getDescription());
    }

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage() && update.getMessage().hasText()
                && update.getMessage().getText().trim().matches("\\d{6}");
    }
}
