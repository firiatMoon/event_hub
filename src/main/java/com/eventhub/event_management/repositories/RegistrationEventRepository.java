package com.eventhub.event_management.repositories;

import com.eventhub.event_management.entities.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationEventRepository extends JpaRepository<RegistrationEntity, Long> {
    RegistrationEntity findByEventId(Long eventId);

    List<RegistrationEntity> findAllByUserId(Long userId);
}
