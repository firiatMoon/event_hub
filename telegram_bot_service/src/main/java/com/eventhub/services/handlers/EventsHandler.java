package com.eventhub.services.handlers;

import com.eventhub.constants.TelegramBotConstant;
import com.eventhub.dto.EventTelegramBotDTO;
import com.eventhub.services.MessageSender;
import com.eventhub.services.TelegramBotService;
import com.eventhub.services.clients.EventManagerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Objects;

@Component
public class EventsHandler implements CommandHandler{
    private final static Logger log = LoggerFactory.getLogger(EventsHandler.class);

    private final MessageSender messageSender;
    private final TelegramBotService telegramBotService;
    private final EventManagerClient eventManagerClient;

    public EventsHandler(MessageSender messageSender, TelegramBotService telegramBotService, EventManagerClient eventManagerClient) {
        this.messageSender = messageSender;
        this.telegramBotService = telegramBotService;
        this.eventManagerClient = eventManagerClient;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.getMessage().getChatId();

        if(!telegramBotService.isChatLinked(chatId)) {
            messageSender.sendMessage(chatId, TelegramBotConstant.AUTH_REQUIRED);
            return;
        }

        String text = update.getMessage().getText().trim();
        boolean onlyToday = Objects.equals("/today", text);

        handleGetEvents(chatId, onlyToday);
    }

    @Override
    public boolean canHandle(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            return Objects.equals("/events", text) || Objects.equals("/today", text);
        }
        return false;
    }

    private void handleGetEvents(Long chatId, boolean onlyToday) {
        Long userId = telegramBotService.getUserIdByChatId(chatId);

        if (Objects.isNull(userId)) {
            messageSender.sendMessage(chatId, TelegramBotConstant.AUTH_REQUIRED);
            return;
        }

        List<EventTelegramBotDTO> events = eventManagerClient.getEvents(userId, onlyToday);
        sendFormattedList(chatId, events, onlyToday);
    }

    private void sendFormattedList(Long chatId, List<EventTelegramBotDTO> events, boolean onlyToday) {
        if (Objects.isNull(events) || events.isEmpty()) {
            messageSender.sendMessage(chatId, onlyToday
                    ? TelegramBotConstant.NO_EVENTS_TODAY
                    : TelegramBotConstant.NO_EVENTS);
            return;
        }

        StringBuilder eventsInfo = new StringBuilder();
        eventsInfo.append(onlyToday ? TelegramBotConstant.EVENTS_TITLE_TODAY : TelegramBotConstant.EVENTS_TITLE_ALL)
                .append("\n\n");

        for (EventTelegramBotDTO event : events) {
            eventsInfo.append("name: ").append(event.name()).append("\n")
                    .append("date: ").append(event.date().toLocalDate()).append("\n")
                    .append("time :").append(event.date().toLocalTime()).append("\n")
                    .append("location: ").append(event.location()).append("\n")
                    .append("status: ").append(event.status())
                    .append("\n\n");
        }

        messageSender.sendMessage(chatId, eventsInfo.toString());
    }
}
