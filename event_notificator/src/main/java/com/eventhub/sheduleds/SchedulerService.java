package com.eventhub.sheduleds;


import com.eventhub.entities.NotificationEntity;
import com.eventhub.repositories.NotificationRepository;
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

    private final NotificationRepository notificationRepository;

    private static final String CRON = "0 0 * * * *";
    private static final Integer MINUS_DAYS = 7;

    public SchedulerService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Scheduled(cron = CRON)
    @Transactional
    public void deleteNotifications() {
        List<NotificationEntity> notifications = notificationRepository
                .findByCreatedAtBefore(LocalDateTime.now()
                        .minusDays(MINUS_DAYS));

        if(notifications.isEmpty()) {
            return;
        }

        try {
            notificationRepository.deleteAll(notifications);
        } catch(Exception e) {
            log.error("Error delete notification: {}", e.getMessage());
        }
    }
}
