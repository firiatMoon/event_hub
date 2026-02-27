package com.eventhub.services.handlers;

import com.eventhub.dto.EventTelegramBotDTO;
import com.eventhub.services.LocaleMessageService;
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
    private final LocaleMessageService messageService;

    public EventsHandler(MessageSender messageSender, TelegramBotService telegramBotService,
                         EventManagerClient eventManagerClient, LocaleMessageService messageService) {
        this.messageSender = messageSender;
        this.telegramBotService = telegramBotService;
        this.eventManagerClient = eventManagerClient;
        this.messageService = messageService;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        String lang = update.getMessage().getFrom().getLanguageCode();

        if(!telegramBotService.isChatLinked(chatId)) {
            messageSender.sendMessage(chatId, messageService.getMessage("bot.auth-required", lang));
            return;
        }

        String text = update.getMessage().getText().trim();
        boolean onlyToday = Objects.equals("/today", text);

        handleGetEvents(chatId, onlyToday, lang);
    }

    @Override
    public boolean canHandle(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            return Objects.equals("/events", text) || Objects.equals("/today", text);
        }
        return false;
    }

    private void handleGetEvents(Long chatId, boolean onlyToday, String lang) {
        Long userId = telegramBotService.getUserIdByChatId(chatId);

        if (Objects.isNull(userId)) {
            messageSender.sendMessage(chatId, messageService.getMessage("bot.auth-required", lang));
            return;
        }

        List<EventTelegramBotDTO> events = eventManagerClient.getEvents(userId, onlyToday);
        sendFormattedList(chatId, events, onlyToday, lang);
    }

    private void sendFormattedList(Long chatId, List<EventTelegramBotDTO> events, boolean onlyToday, String lang) {
        if (Objects.isNull(events) || events.isEmpty()) {
            messageSender.sendMessage(chatId, onlyToday
                    ? messageService.getMessage("bot.no-events-today", lang)
                    : messageService.getMessage("bot.no-events", lang));
            return;
        }

        StringBuilder eventsInfo = new StringBuilder();
        eventsInfo.append(onlyToday
                        ?  messageService.getMessage("bot.events-title-today", lang)
                        : messageService.getMessage("bot.events-title-all", lang))
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
