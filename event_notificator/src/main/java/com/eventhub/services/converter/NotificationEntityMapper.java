package com.eventhub.services.converter;

import com.eventhub.dto.NotificationDTO;
import com.eventhub.entities.NotificationEntity;
import com.eventhub.enums.EventStatus;
import com.eventhub.kafka.events.EventChangeKafkaMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class NotificationEntityMapper {

    public NotificationDTO toNotificationDTO(NotificationEntity notificationEntity) {
        return new NotificationDTO(
                notificationEntity.getEventId(),
                notificationEntity.getOldName(),
                notificationEntity.getNewName(),
                notificationEntity.getOldMaxPlaces(),
                notificationEntity.getNewMaxPlaces(),
                notificationEntity.getOldDate(),
                notificationEntity.getNewDate(),
                notificationEntity.getOldCost(),
                notificationEntity.getNewCost(),
                notificationEntity.getOldDuration(),
                notificationEntity.getNewDuration(),
                notificationEntity.getOldLocationId(),
                notificationEntity.getNewLocationId(),
                notificationEntity.getOldStatus(),
                notificationEntity.getNewStatus()
        );
    }

    public NotificationEntity toCreateNotification(EventChangeKafkaMessage eventChangeKafkaMessage){
        NotificationEntity notificationEntity = new NotificationEntity();

        notificationEntity.setCreatedAt(LocalDateTime.now());
        notificationEntity.setRead(false);
        notificationEntity.setEventId(eventChangeKafkaMessage.getEventId());
        notificationEntity.setOwnerId(eventChangeKafkaMessage.getOwnerId());

        if (!Objects.isNull(eventChangeKafkaMessage.getName())) {
            notificationEntity.setOldName(eventChangeKafkaMessage.getName().getOldField());
            notificationEntity.setNewName(eventChangeKafkaMessage.getName().getNewField());
        }

        if (!Objects.isNull(eventChangeKafkaMessage.getMaxPlaces())) {
            notificationEntity.setOldMaxPlaces(eventChangeKafkaMessage.getMaxPlaces().getOldField());
            notificationEntity.setNewMaxPlaces(eventChangeKafkaMessage.getMaxPlaces().getNewField());
        }

        if (!Objects.isNull(eventChangeKafkaMessage.getDate())) {
            notificationEntity.setOldDate(eventChangeKafkaMessage.getDate().getOldField());
            notificationEntity.setNewDate(eventChangeKafkaMessage.getDate().getNewField());
        }

        if (!Objects.isNull(eventChangeKafkaMessage.getCost())) {
            notificationEntity.setOldCost(eventChangeKafkaMessage.getCost().getOldField());
            notificationEntity.setNewCost(eventChangeKafkaMessage.getCost().getNewField());
        }

        if (!Objects.isNull(eventChangeKafkaMessage.getDuration())) {
            notificationEntity.setOldDuration(eventChangeKafkaMessage.getDuration().getOldField());
            notificationEntity.setNewDuration(eventChangeKafkaMessage.getDuration().getNewField());
        }

        if (!Objects.isNull(eventChangeKafkaMessage.getLocationId())) {
            notificationEntity.setOldLocationId(eventChangeKafkaMessage.getLocationId().getOldField());
            notificationEntity.setNewLocationId(eventChangeKafkaMessage.getLocationId().getNewField());
        }

        if (!Objects.isNull(eventChangeKafkaMessage.getStatus())) {
            notificationEntity.setOldStatus(EventStatus.valueOf(eventChangeKafkaMessage.getStatus().getOldField()));
            notificationEntity.setNewStatus(EventStatus.valueOf(eventChangeKafkaMessage.getStatus().getNewField()));
        }

        return notificationEntity;
    }
}
