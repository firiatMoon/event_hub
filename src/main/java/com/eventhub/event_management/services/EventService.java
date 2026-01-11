package com.eventhub.event_management.services;

import com.eventhub.event_management.dto.EventRequestDto;
import com.eventhub.event_management.dto.EventSearchRequestDto;
import com.eventhub.event_management.entities.EventEntity;
import com.eventhub.event_management.entities.RegistrationEntity;
import com.eventhub.event_management.enums.EventStatus;
import com.eventhub.event_management.enums.Role;
import com.eventhub.event_management.repositories.EventRepository;
import com.eventhub.event_management.repositories.RegistrationEventRepository;
import com.eventhub.event_management.security.jwt.JwtTokenManager;
import com.eventhub.event_management.services.converter.EventEntityConverted;
import com.eventhub.event_management.vo.Event;
import com.eventhub.event_management.vo.Location;
import com.eventhub.event_management.vo.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final RegistrationEventRepository registrationEventRepository;
    private final UserService userService;
    private final LocationService locationService;
    private final EventEntityConverted eventEntityConverted;
    private final JwtTokenManager jwtTokenManager;

    public EventService(EventRepository eventRepository, RegistrationEventRepository registrationEventRepository, UserService userService, LocationService locationService, EventEntityConverted eventEntityConverted, JwtTokenManager jwtTokenManager) {
        this.eventRepository = eventRepository;
        this.registrationEventRepository = registrationEventRepository;
        this.userService = userService;
        this.locationService = locationService;
        this.eventEntityConverted = eventEntityConverted;
        this.jwtTokenManager = jwtTokenManager;
    }

    @Transactional
    public Event createEvent(HttpServletRequest request, EventRequestDto eventRequestDto) {
        User user = getLoginFromRequest(request);
        Location location = locationService.getLocationById(eventRequestDto.locationId());

        if (eventRequestDto.maxPlaces().compareTo(location.capacity()) > 0) {
            throw new IllegalArgumentException("The location cannot accommodate such a number of participants.");
        }

        EventEntity eventEntity = new EventEntity();
        eventEntity.setName(eventRequestDto.name());
        eventEntity.setOwnerId(user.id());
        eventEntity.setOccupiedPlaces(0);
        eventEntity.setMaxPlaces(eventRequestDto.maxPlaces());
        eventEntity.setDate(stringToLocalDateTime(eventRequestDto.date()));
        eventEntity.setCost(eventRequestDto.cost());
        eventEntity.setDuration(eventRequestDto.duration());
        eventEntity.setLocationId(location.id());
        eventEntity.setStatus(EventStatus.WAIT_START);

        eventRepository.save(eventEntity);
        return eventEntityConverted.toEvent(eventEntity);
    }

    private LocalDateTime stringToLocalDateTime(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private User getLoginFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String login = jwtTokenManager.getLoginFromToken(bearerToken.substring(7));
        return userService.findByLogin(login);
    }

    @Transactional
    public void deleteEvent(HttpServletRequest request, Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException("Event not found.");
        }

        User user = getLoginFromRequest(request);
        Event event = getEventById(eventId);

        if (!rightToDoAnything(user, event)) {
            throw new EntityNotFoundException("There are no rights to cancel the event.");
        }

        if (event.status().equals(EventStatus.STARTED)) {
            throw new EntityNotFoundException("The event cannot be deleted. Event has been started.");
        }

        if (event.status().equals(EventStatus.FINISHED)) {
            throw new EntityNotFoundException("The event cannot be deleted. Event has been finished.");
        }

        if (Objects.equals(user.id(), event.ownerId()) && event.status().equals(EventStatus.WAIT_START)) {
            EventEntity eventEntity = eventEntityConverted.toEventEntity(event);
            eventEntity.setStatus(EventStatus.CANCELLED);
            eventRepository.save(eventEntity);
        }
    }

    @Transactional
    public Event updateEvent(HttpServletRequest request, Long eventId, EventRequestDto eventRequestDto) {
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException("Event not found.");
        }

        User user = getLoginFromRequest(request);
        Event event = getEventById(eventId);
        Location location = locationService.getLocationById(eventRequestDto.locationId());

        if (!rightToDoAnything(user, event)) {
            throw new EntityNotFoundException("There are no rights to cancel the event.");
        }

        if (event.status().equals(EventStatus.STARTED)) {
            throw new EntityNotFoundException("The event cannot be updated. Event has been started.");
        }

        if (eventRequestDto.maxPlaces().compareTo(location.capacity()) > 0) {
            throw new IllegalArgumentException("The location cannot accommodate such a number of participants.");
        }

        if (event.occupiedPlaces().compareTo(eventRequestDto.maxPlaces()) > 0) {
            throw new IllegalArgumentException("More participants have already registered for the event.");
        }

        Event updatedEvent = new Event(event.id(), eventRequestDto.name(), event.ownerId(), event.occupiedPlaces(),
                eventRequestDto.maxPlaces(), stringToLocalDateTime(eventRequestDto.date()), eventRequestDto.cost(),
                eventRequestDto.duration(), eventRequestDto.locationId(), event.status());

        EventEntity eventEntity = eventEntityConverted.toEventEntity(updatedEvent);
        eventRepository.save(eventEntity);

        return updatedEvent;
    }

    @Transactional
    public void registrationEvent(HttpServletRequest request, Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException("Event not found.");
        }
        User user = getLoginFromRequest(request);
        Event event = getEventById(eventId);
        if(event.status().equals(EventStatus.WAIT_START) && !Objects.equals(event.occupiedPlaces(), event.maxPlaces())) {
            RegistrationEntity registrationEntity = new RegistrationEntity();
            registrationEntity.setUserId(user.id());
            registrationEntity.setEvent(eventEntityConverted.toEventEntity(event));
            registrationEventRepository.save(registrationEntity);
        } else {
            throw new IllegalArgumentException("The event cannot be registered.");
        }
    }

    public void deleteRegistrationEvent(HttpServletRequest request, Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException("Event not found.");
        }
        User user = getLoginFromRequest(request);
        Event event = getEventById(eventId);

        if(event.status().equals(EventStatus.WAIT_START)) {
            List<RegistrationEntity> registrationEntities = eventEntityConverted.toEventEntity(event).getRegistrations();
            Long registrationId = registrationEntities.stream()
                    .filter(registration -> registration.getUserId().equals(user.id()))
                    .map(RegistrationEntity::getId)
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Registration not found."));
            registrationEventRepository.deleteById(registrationId);
        } else {
            throw new IllegalArgumentException("The event cannot be deleted.");
        }
    }

    public List<Event> getAllEvents(EventSearchRequestDto searchRequestDto) {
        return eventRepository.findEventsByFilter(
                        searchRequestDto.name(),
                        searchRequestDto.placesMin(),
                        searchRequestDto.placesMax(),
                        searchRequestDto.dateStartAfter(),
                        searchRequestDto.dateStartBefore(),
                        searchRequestDto.costMin(),
                        searchRequestDto.costMax(),
                        searchRequestDto.durationMin(),
                        searchRequestDto.durationMax(),
                        searchRequestDto.locationId(),
                        searchRequestDto.eventStatus())
                .stream()
                .map(eventEntityConverted::toEvent).toList();
    }

    public List<EventEntity> findAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getMyEvents(HttpServletRequest request) {
        User user = getLoginFromRequest(request);
        List<EventEntity> listAllEvents = eventRepository.findAll().stream().
                filter(item -> item.getOwnerId().equals(user.id()))
                .toList();
        return listAllEvents.stream()
                .map(eventEntityConverted::toEvent)
                .toList();
    }

    private boolean rightToDoAnything(User user, Event event) {
        boolean isAdmin = user.role().equals(Role.ADMIN);
        boolean isOwner = Objects.equals(user.id(), event.ownerId());

        return isAdmin || isOwner;
    }

    public Event getEventById(Long eventId) {
        EventEntity foundEventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("No event found with id: " + eventId));
        return eventEntityConverted.toEvent(foundEventEntity);
    }


}
