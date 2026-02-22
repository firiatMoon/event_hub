package com.eventhub.entities;

import com.eventhub.model.Event;
import jakarta.persistence.*;

@Entity
@Table(name = "registration_events")
public class RegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private EventEntity event;

    public RegistrationEntity(EventEntity event, Long userId, Long id) {
        this.event = event;
        this.userId = userId;
        this.id = id;
    }

    public RegistrationEntity() {
    }

    public RegistrationEntity(Long id, Long userId, Event event) {
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

    public EventEntity getEvent() {
        return event;
    }

    public void setEvent(EventEntity event) {
        this.event = event;
    }
}
