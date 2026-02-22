package com.eventhub.services;

import com.eventhub.dto.NotificationDTO;
import com.eventhub.entities.NotificationEntity;
import com.eventhub.kafka.events.EventChangeKafkaMessage;
import com.eventhub.repositories.NotificationRepository;
import com.eventhub.services.converter.NotificationEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class NotificationsService {

    private static final Logger log = LoggerFactory.getLogger(NotificationsService.class);

    private final NotificationEntityMapper notificationEntityMapper;
    private final NotificationRepository notificationRepository;

    public NotificationsService(NotificationEntityMapper notificationEntityMapper,
                                NotificationRepository notificationRepository) {
        this.notificationEntityMapper = notificationEntityMapper;
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public void creatNotification(EventChangeKafkaMessage event) {
        List<Long> registrationUsers = event.getSubscribersToEvent();

        if (Objects.isNull(registrationUsers) || registrationUsers.isEmpty()) {
            log.info("No subscribers to notify for event {}", event.getEventId());
            throw new NoSuchElementException("The list contains no elements.");
        }

        NotificationEntity template = notificationEntityMapper.toCreateNotification(event);

        List<NotificationEntity> notifications = registrationUsers
                .stream()
                .map(userId -> {
                    NotificationEntity entity = notificationEntityMapper.toCreateNotification(event);
                    entity.setUserId(userId);
                    return entity;
                })
                .toList();
        notificationRepository.saveAll(notifications);

    }

    public List<NotificationDTO> findAllNotReadNotification() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication) || Objects.isNull(authentication.getDetails())) {
            throw new BadCredentialsException("The user is not authenticated.");
        }

        Long userId = (Long) authentication.getDetails();

        return notificationRepository
                .findAllByUserId(userId)
                .stream()
                .filter(item -> !item.getRead())
                .map(notificationEntityMapper::toNotificationDTO)
                .toList();
    }

    @Transactional
    public void notificationRead(List<Long> notificationIds) {
        List<NotificationEntity> notificationEntities = notificationRepository.findAllById(notificationIds);

        if (notificationIds.isEmpty()) {
            throw new NoSuchElementException("The list contains no elements.");
        }

        for (NotificationEntity event : notificationEntities) {
            if (event.getRead().equals(false)) {
                event.setRead(true);
                notificationRepository.save(event);
                log.info("The notification {} is read.", event.getId());
            }
        }
    }
}
