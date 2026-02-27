package com.eventhub.repositories;

import com.eventhub.entities.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationEventRepository extends JpaRepository<RegistrationEntity, Long> {

    @Query("SELECT DISTINCT re.userId FROM RegistrationEntity re WHERE re.event.id = :eventId")
    List<Long> findUsersRegisteredForEvent(@Param("eventId") Long eventId);

    List<RegistrationEntity> findAllByUserId(Long userId);
}
