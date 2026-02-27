package com.eventhub.kafka.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class EventChangeKafkaMessage {
    //ID мероприятия
    private Long eventId;

    //ID пользователя, который внес изменения (может быть null, если изменение сделано системой)
    private Long idUserChanged;

    //ID владельца мероприятия
    private Long ownerId;

    //Список подписчиков данного мероприятия
    private List<Long> subscribersToEvent;

    // Информация о полях, которые были изменены (старые значения и новые значения)
    private EventFieldChange<String> name;
    private EventFieldChange<Integer> maxPlaces;
    private EventFieldChange<LocalDateTime> date;
    private EventFieldChange<BigDecimal> cost;
    private EventFieldChange<Integer> duration;
    private EventFieldChange<Long> locationId;
    private EventFieldChange<String> status;

    private String languageCode;

    public EventChangeKafkaMessage() {
    }

    public EventChangeKafkaMessage(Long eventId, Long idUserChanged, Long ownerId, List<Long> subscribersToEvent,
                                   EventFieldChange<String> name, EventFieldChange<Integer> maxPlaces,
                                   EventFieldChange<LocalDateTime> date, EventFieldChange<BigDecimal> cost,
                                   EventFieldChange<Integer> duration, EventFieldChange<Long> locationId,
                                   EventFieldChange<String> status, String languageCode) {
        this.eventId = eventId;
        this.idUserChanged = idUserChanged;
        this.ownerId = ownerId;
        this.subscribersToEvent = subscribersToEvent;
        this.name = name;
        this.maxPlaces = maxPlaces;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.locationId = locationId;
        this.status = status;
        this.languageCode = languageCode;
    }


    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getIdUserChanged() {
        return idUserChanged;
    }

    public void setIdUserChanged(Long idUserChanged) {
        this.idUserChanged = idUserChanged;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Long> getSubscribersToEvent() {
        return subscribersToEvent;
    }

    public void setSubscribersToEvent(List<Long> subscribersToEvent) {
        this.subscribersToEvent = subscribersToEvent;
    }

    public EventFieldChange<String> getName() {
        return name;
    }

    public void setName(EventFieldChange<String> name) {
        this.name = name;
    }

    public EventFieldChange<Integer> getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(EventFieldChange<Integer> maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public EventFieldChange<LocalDateTime> getDate() {
        return date;
    }

    public void setDate(EventFieldChange<LocalDateTime> date) {
        this.date = date;
    }

    public EventFieldChange<BigDecimal> getCost() {
        return cost;
    }

    public void setCost(EventFieldChange<BigDecimal> cost) {
        this.cost = cost;
    }

    public EventFieldChange<Integer> getDuration() {
        return duration;
    }

    public void setDuration(EventFieldChange<Integer> duration) {
        this.duration = duration;
    }

    public EventFieldChange<Long> getLocationId() {
        return locationId;
    }

    public void setLocationId(EventFieldChange<Long> locationId) {
        this.locationId = locationId;
    }

    public EventFieldChange<String> getStatus() {
        return status;
    }

    public void setStatus(EventFieldChange<String> status) {
        this.status = status;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}
