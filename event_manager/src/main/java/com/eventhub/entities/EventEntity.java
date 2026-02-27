package com.eventhub.entities;


import com.eventhub.enums.EventStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "events")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "owner_id", nullable = true)
    private Long ownerId;

    @Column(name = "occupied_places")
    private Integer occupiedPlaces;

    @Column(name = "max_place", nullable = true)
    private Integer maxPlaces;

    @Column(name = "date", nullable = true)
    private LocalDateTime date;

    @Column(name = "cost", nullable = true)
    private BigDecimal cost;

    @Column(name = "duration", nullable = true)
    private Integer duration;

    @Column(name = "location_id", nullable = true)
    private Long locationId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistrationEntity> registrations = new ArrayList<>();

    public EventEntity() {
    }

    public EventEntity(Long id, String name, Long ownerId, Integer occupiedPlaces, Integer maxPlaces,
                       LocalDateTime date, BigDecimal cost, Integer duration, Long locationId,
                       EventStatus status) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.occupiedPlaces = occupiedPlaces;
        this.maxPlaces = maxPlaces;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.locationId = locationId;
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Event{name=%s, owner_id=%s, occupied_places=%s, max_place=%s, date=%s, cost=%s, " +
                        "duration=%s, location_id=%s, status=%s}", name, ownerId, occupiedPlaces, maxPlaces, date, cost,
                duration, locationId, status);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EventEntity eventEntity = (EventEntity) obj;
        return Objects.equals(eventEntity.getName(), name) &&
                Objects.equals(eventEntity.getOwnerId(), ownerId) &&
                Objects.equals(eventEntity.getOccupiedPlaces(), occupiedPlaces) &&
                Objects.equals(eventEntity.getMaxPlaces(), maxPlaces) &&
                Objects.equals(eventEntity.getDate(), date) &&
                cost.compareTo(eventEntity.getCost()) == 0 &&
                Objects.equals(eventEntity.getDuration(), duration) &&
                Objects.equals(eventEntity.getLocationId(), locationId) &&
                Objects.equals(eventEntity.getStatus(), status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ownerId, occupiedPlaces, maxPlaces, date, cost, duration, locationId, status);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getOccupiedPlaces() {
        return occupiedPlaces;
    }

    public void setOccupiedPlaces(Integer occupiedPlaces) {
        this.occupiedPlaces = occupiedPlaces;
    }

    public Integer getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(Integer maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public List<RegistrationEntity> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<RegistrationEntity> registrations) {
        this.registrations = registrations;
    }
}



