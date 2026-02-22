package com.eventhub.sheduleds;


import com.eventhub.entities.EventEntity;
import com.eventhub.enums.EventStatus;
import com.eventhub.repositories.EventRepository;
import com.eventhub.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
public class SchedulerService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

    private final EventService eventService;
    private final EventRepository eventRepository;

    private static final String CRON = "* */10 * * * *";

    public SchedulerService(EventService eventService, EventRepository eventRepository) {
        this.eventService = eventService;
        this.eventRepository = eventRepository;
    }

    @Scheduled(cron = CRON)
    @Transactional
    public void updateStatusEvents() {
        List<EventEntity> events = eventService.findAllEvents().stream()
                .filter(status -> Objects.equals(status.getStatus(), EventStatus.STARTED))
                .toList();

        if(events.isEmpty()) {
            return;
        }

        try {
            LocalDateTime now = LocalDateTime.now();

            for (EventEntity event : events) {
                if (event.getDate().plusMinutes(event.getDuration()).isBefore(now)) {
                    event.setStatus(EventStatus.FINISHED);
                    eventRepository.save(event);
                    log.info("The event {} is over.", event.getId());
                }
            }
        } catch(Exception e) {
            log.error("Error updating event status:", e);
        }
    }
}
