package com.eventhub.kafka;

import com.eventhub.kafka.events.EventChangeKafkaMessage;
import com.eventhub.services.NotificationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@KafkaListener(topics = "${kafka.topics.event-change}", containerFactory = "containerFactory")
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    private final NotificationsService notificationService;

    public KafkaConsumer(NotificationsService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaHandler
    public void handler(EventChangeKafkaMessage event) {
        log.info("Received event: even_id - {}, {}", event.getEventId(), event.toString());

        try{
            notificationService.creatNotification(event);
        } catch (Exception e) {
            log.error("Event not received: {}, {}", event.getEventId(), e.getMessage());
        }
    }
}
