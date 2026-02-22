package com.eventhub.services;


import com.eventhub.constants.TelegramBotConstant;
import com.eventhub.services.handlers.CommandHandler;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final static Logger log = LoggerFactory.getLogger(UpdateConsumer.class);

    private final List<CommandHandler> handlers;

    private final TelegramClient telegramClient;
    private final MessageSender messageSender;

    public UpdateConsumer(List<CommandHandler> handlers, TelegramClient telegramClient, MessageSender messageSender) {
        this.handlers = handlers;
        this.telegramClient = telegramClient;
        this.messageSender = messageSender;
    }

    @PostConstruct
    public void setBotMenu() {
        List<BotCommand> commands = List.of(
                new BotCommand("/events", "View all your registered events"),
                new BotCommand("/today", "View events scheduled for today"),
                new BotCommand("/help", "Get help and command list"),
                new BotCommand("/inlink", "Delete account")
        );

        try {
            SetMyCommands setMyCommands = SetMyCommands.builder()
                    .commands(commands)
                    .build();

            telegramClient.execute(setMyCommands);

            log.info("Telegram bot menu has been successfully configured!");
        } catch (TelegramApiException ex) {
            log.error("Failed to set bot commands menu: {}", ex.getMessage());
        }
    }

    @Override
    public void consume(Update update) {
        try {
            handlers.stream()
                    .filter(handler -> handler.canHandle(update))
                    .findFirst()
                    .ifPresentOrElse(handler -> handler.handle(update),
                            () -> handleUnknownUpdate(update));
        } catch (Exception ex) {
            log.error("Failed to process command: {}", update.getMessage());
            handleBotException(update, ex);
        }
    }

    private void handleUnknownUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            messageSender.sendMessage(chatId, TelegramBotConstant.INVALID_COMMAND);
        }
    }

    private void handleBotException(Update update, Exception exception) {
        Long chatId = update.hasCallbackQuery()
                ? update.getCallbackQuery().getMessage().getChatId()
                : update.getMessage().getChatId();

        if (exception instanceof HttpClientErrorException.NotFound) {
            messageSender.sendMessage(chatId, TelegramBotConstant.ERR_DATA_NOT_FOUND);
        } else if (exception instanceof HttpClientErrorException.Unauthorized) {
            messageSender.sendMessage(chatId, TelegramBotConstant.ERR_AUTH_FAILED);
        } else {
            log.error("Unhandled bot exception for chatId {}", chatId, exception);
            messageSender.sendMessage(chatId, TelegramBotConstant.ERR_UNEXPECTED_SERVER);
        }
    }
}
