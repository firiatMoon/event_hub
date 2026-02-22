package com.eventhub.entities;


import com.eventhub.enums.EventStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "owner_id", nullable = true)
    private Long ownerId;

    @Column(name = "old_name", nullable = true)
    private String oldName;

    @Column(name = "new_name", nullable = true)
    private String newName;

    @Column(name = "old_max_place", nullable = true)
    private Integer oldMaxPlaces;

    @Column(name = "new_max_place", nullable = true)
    private Integer newMaxPlaces;

    @Column(name = "old_date", nullable = true)
    private LocalDateTime oldDate;

    @Column(name = "new_date", nullable = true)
    private LocalDateTime newDate;

    @Column(name = "old_cost", nullable = true)
    private BigDecimal oldCost;

    @Column(name = "new_cost", nullable = true)
    private BigDecimal newCost;

    @Column(name = "old_duration", nullable = true)
    private Integer oldDuration;

    @Column(name = "new_duration", nullable = true)
    private Integer newDuration;

    @Column(name = "old_location_id", nullable = true)
    private Long oldLocationId;

    @Column(name = "new_location_id", nullable = true)
    private Long newLocationId;

    @Column(name = "old_status")
    @Enumerated(EnumType.STRING)
    private EventStatus oldStatus;

    @Column(name = "new_status")
    @Enumerated(EnumType.STRING)
    private EventStatus newStatus;


    public NotificationEntity() {
    }

    public NotificationEntity(Long id, Long userId, LocalDateTime createdAt, Boolean isRead, Long eventId, Long ownerId,
                              String oldName, String newName, Integer oldMaxPlaces, Integer newMaxPlaces,
                              LocalDateTime oldDate, LocalDateTime newDate, BigDecimal oldCost, BigDecimal newCost,
                              Integer oldDuration, Integer newDuration, Long oldLocationId, Long newLocationId,
                              EventStatus oldStatus, EventStatus newStatus) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.isRead = isRead;
        this.eventId = eventId;
        this.ownerId = ownerId;
        this.oldName = oldName;
        this.newName = newName;
        this.oldMaxPlaces = oldMaxPlaces;
        this.newMaxPlaces = newMaxPlaces;
        this.oldDate = oldDate;
        this.newDate = newDate;
        this.oldCost = oldCost;
        this.newCost = newCost;
        this.oldDuration = oldDuration;
        this.newDuration = newDuration;
        this.oldLocationId = oldLocationId;
        this.newLocationId = newLocationId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }


    @Override
    public String toString() {
        return String.format("Notification{evenId= %s; NAME: old=%s, new=%s; MAX_PLACE: old=%s, new=%s; DATE: old=%s, " +
                        "new=%s; COST: old=%s, new=%s; DURATION: old=%s, new=%s; LOCATION_ID: old=%s, new=%s; " +
                        "STATUS: old=%s, new=%s}", getEventId(), getOldName(), getNewName(), getOldMaxPlaces(),
                getNewMaxPlaces(), getOldDate(), getNewDate(), getOldCost(), getNewCost(), getOldDuration(),
                getNewDuration(), getOldLocationId(), getNewLocationId(), getOldStatus(), getNewStatus());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NotificationEntity notificationEntity = (NotificationEntity) obj;
        return Objects.equals(notificationEntity.getEventId(), eventId) &&
                Objects.equals(notificationEntity.getOldName(), oldName) &&
                Objects.equals(notificationEntity.getNewName(), newName) &&
                Objects.equals(notificationEntity.getOldMaxPlaces(), oldMaxPlaces) &&
                Objects.equals(notificationEntity.getNewMaxPlaces(), newMaxPlaces) &&
                Objects.equals(notificationEntity.getOldDate(), oldDate) &&
                Objects.equals(notificationEntity.getNewDate(), newDate) &&
                oldCost.compareTo(notificationEntity.getOldCost()) == 0 &&
                newCost.compareTo(notificationEntity.getNewCost()) == 0 &&
                Objects.equals(notificationEntity.getOldDuration(), oldDuration) &&
                Objects.equals(notificationEntity.getNewDuration(), newDuration) &&
                Objects.equals(notificationEntity.getOldLocationId(), oldLocationId) &&
                Objects.equals(notificationEntity.getNewLocationId(), newLocationId) &&
                Objects.equals(notificationEntity.getOldStatus(), oldStatus) &&
                Objects.equals(notificationEntity.getNewStatus(), newStatus);
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hash(eventId, oldName, newName, oldMaxPlaces, newMaxPlaces, oldDate, newDate,
                oldCost, newCost, oldDuration, newDuration, oldLocationId, newLocationId, oldStatus, newStatus);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public Integer getOldMaxPlaces() {
        return oldMaxPlaces;
    }

    public void setOldMaxPlaces(Integer oldMaxPlaces) {
        this.oldMaxPlaces = oldMaxPlaces;
    }

    public Integer getNewMaxPlaces() {
        return newMaxPlaces;
    }

    public void setNewMaxPlaces(Integer newMaxPlaces) {
        this.newMaxPlaces = newMaxPlaces;
    }

    public LocalDateTime getOldDate() {
        return oldDate;
    }

    public void setOldDate(LocalDateTime oldDate) {
        this.oldDate = oldDate;
    }

    public LocalDateTime getNewDate() {
        return newDate;
    }

    public void setNewDate(LocalDateTime newDate) {
        this.newDate = newDate;
    }

    public BigDecimal getOldCost() {
        return oldCost;
    }

    public void setOldCost(BigDecimal oldCost) {
        this.oldCost = oldCost;
    }

    public BigDecimal getNewCost() {
        return newCost;
    }

    public void setNewCost(BigDecimal newCost) {
        this.newCost = newCost;
    }

    public Integer getOldDuration() {
        return oldDuration;
    }

    public void setOldDuration(Integer oldDuration) {
        this.oldDuration = oldDuration;
    }

    public Integer getNewDuration() {
        return newDuration;
    }

    public void setNewDuration(Integer newDuration) {
        this.newDuration = newDuration;
    }

    public Long getOldLocationId() {
        return oldLocationId;
    }

    public void setOldLocationId(Long oldLocationId) {
        this.oldLocationId = oldLocationId;
    }

    public Long getNewLocationId() {
        return newLocationId;
    }

    public void setNewLocationId(Long newLocationId) {
        this.newLocationId = newLocationId;
    }

    public EventStatus getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(EventStatus oldStatus) {
        this.oldStatus = oldStatus;
    }

    public EventStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(EventStatus newStatus) {
        this.newStatus = newStatus;
    }
}



