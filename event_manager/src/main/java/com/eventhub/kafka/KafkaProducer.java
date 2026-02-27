package com.eventhub.kafka;


import com.eventhub.kafka.events.EventChangeKafkaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    @Value("${kafka.topics.event-change}")
    private String topicEventChange;

    private final KafkaTemplate<Long, EventChangeKafkaMessage> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<Long, EventChangeKafkaMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(EventChangeKafkaMessage event) {
        kafkaTemplate.send(topicEventChange, event.getOwnerId(), event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Successfully sent: {}; owner_id: {}", event, event.getOwnerId());
                    } else {
                        log.error("Failed to send message to Kafka: {}", ex.getMessage());
                    }
                });
    }
}
