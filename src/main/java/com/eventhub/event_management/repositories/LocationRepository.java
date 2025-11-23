package com.eventhub.event_management.repositories;

import com.eventhub.event_management.entities.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

//    @Query("""
//    SELECT l FROM Location l
//    """)
//    List<Location> searchLocations(Pageable pageable);

    Page<Location> findAll(Pageable pageable);
}
