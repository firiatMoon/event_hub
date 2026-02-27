package com.eventhub.services.handlers;

import com.eventhub.services.LocaleMessageService;
import com.eventhub.services.MessageSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class HelpHandler implements CommandHandler {
    private final MessageSender messageSender;
    private final LocaleMessageService messageService;

    public HelpHandler(MessageSender messageSender, LocaleMessageService messageService) {
        this.messageSender = messageSender;
        this.messageService = messageService;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        String lang = update.getMessage().getFrom().getLanguageCode();
        messageSender.sendMessage(chatId, messageService.getMessage("bot.help-message", lang));
    }

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage() && update.getMessage().hasText()
                && update.getMessage().getText().startsWith("/help");
    }
}
