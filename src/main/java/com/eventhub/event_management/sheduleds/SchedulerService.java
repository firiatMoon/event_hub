package com.eventhub.event_management.sheduleds;


import com.eventhub.event_management.controllers.UserController;
import com.eventhub.event_management.entities.EventEntity;
import com.eventhub.event_management.enums.EventStatus;
import com.eventhub.event_management.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SchedulerService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

    private final EventService eventService;

    private static final String CRON = "* */10 * * * *";

    public SchedulerService(EventService eventService) {
        this.eventService = eventService;
    }

    @Scheduled(cron = CRON)
    @Transactional
    public void updateStatusEvents() {
        List<EventEntity> events = eventService.findAllEvents().stream()
                .filter(status -> status.getStatus().equals(EventStatus.STARTED))
                .toList();

        if(events.isEmpty()) {
            return;
        }

        try {
            LocalDateTime now = LocalDateTime.now();

            for (EventEntity event : events) {
                if (event.getDate().plusMinutes(event.getDuration()).isBefore(now)) {
                    event.setStatus(EventStatus.FINISHED);
                    log.info("The event {} is over.", event.getId());
                }
            }
        } catch(Exception e) {
            log.error("Error updating event status:", e);
        }
    }
}
