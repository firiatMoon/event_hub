package com.eventhub.kafka;

import com.eventhub.kafka.events.EventChangeKafkaMessage;
import com.eventhub.services.TelegramBotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@KafkaListener(topics = "${kafka.topics.event-change}", containerFactory = "containerFactory")
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    private final TelegramBotService telegramBotService;

    public KafkaConsumer(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    @KafkaHandler
    public void handler(EventChangeKafkaMessage event) {
        log.info("Received event: even_id - {}, {}", event.getEventId(), event.toString());

        try{
            telegramBotService.notificationOfChanges(event);
        } catch (Exception e) {
            log.error("Event not received: {}, {}", event.getEventId(), e.getMessage());
        }
    }
}
