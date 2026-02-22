package com.eventhub.repositories;

import com.eventhub.entities.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findAllByUserId(Long userId);

    List<NotificationEntity> findByCreatedAtBefore(LocalDateTime date);
}
