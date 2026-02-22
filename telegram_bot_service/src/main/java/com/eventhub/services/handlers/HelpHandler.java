package com.eventhub.services.handlers;

import com.eventhub.constants.TelegramBotConstant;
import com.eventhub.services.MessageSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class HelpHandler implements CommandHandler {
    private final MessageSender messageSender;

    public HelpHandler(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        messageSender.sendMessage(chatId, TelegramBotConstant.HELP_MESSAGE);
    }

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage() && update.getMessage().hasText()
                && update.getMessage().getText().startsWith("/help");
    }
}
