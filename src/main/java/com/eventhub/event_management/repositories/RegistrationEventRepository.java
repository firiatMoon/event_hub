package com.eventhub.event_management.repositories;

import com.eventhub.event_management.entities.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationEventRepository extends JpaRepository<RegistrationEntity, Long> {
    RegistrationEntity findByEventId(Long eventId);
}
