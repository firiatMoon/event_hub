package com.eventhub.event_management.services;

import com.eventhub.event_management.entities.EventEntity;
import com.eventhub.event_management.enums.EventStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class EventSpecification {

    public static Specification<EventEntity> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<EventEntity> greaterThanOrEqualToPlace(Integer placesMin) {
        return ((root, query, criteriaBuilder) ->
                placesMin == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("maxPlaces"), placesMin));
    }

    public static Specification<EventEntity> lessThanOrEqualToPlace(Integer placesMax) {
        return ((root, query, criteriaBuilder) ->
                placesMax == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("maxPlaces"), placesMax));
    }

    public static Specification<EventEntity> equalsDateStartAfter(LocalDateTime dateStartAfter) {
        return ((root, query, criteriaBuilder) ->
                dateStartAfter == null ? null : criteriaBuilder.equal(root.get("date"), dateStartAfter));
    }

    public static Specification<EventEntity> equalsDateStartBefore(LocalDateTime dateStartBefore) {
        return ((root, query, criteriaBuilder) ->
                dateStartBefore == null ? null : criteriaBuilder.equal(root.get("date"), dateStartBefore));
    }

    public static Specification<EventEntity> greaterThanOrEqualToCost(BigDecimal costMin) {
        return ((root, query, criteriaBuilder) ->
                costMin == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("cost"), costMin));
    }

    public static Specification<EventEntity> lessThanOrEqualToCost(BigDecimal costMax) {
        return ((root, query, criteriaBuilder) ->
                costMax == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("cost"), costMax));
    }

    public static Specification<EventEntity> greaterThanOrEqualToDuration(Integer durationMin) {
        return ((root, query, criteriaBuilder) ->
                durationMin == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("duration"), durationMin));
    }

    public static Specification<EventEntity> lessThanOrEqualToDuration(Integer durationMax) {
        return ((root, query, criteriaBuilder) ->
                durationMax == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("duration"), durationMax));
    }

    public static Specification<EventEntity> hasLocationId(Long locationId) {
        return ((root, query, criteriaBuilder) ->
                locationId == null ? null : criteriaBuilder.equal(root.get("locationId"), locationId));
    }

    public static Specification<EventEntity> hasEventStatus(EventStatus status) {
        return ((root, query, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"), status));
    }
}




