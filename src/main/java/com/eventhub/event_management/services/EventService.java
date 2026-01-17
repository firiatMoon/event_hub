package com.eventhub.event_management.services;

import com.eventhub.event_management.dto.EventDTO;
import com.eventhub.event_management.dto.EventRequestDto;
import com.eventhub.event_management.dto.EventSearchRequestDto;
import com.eventhub.event_management.entities.EventEntity;
import com.eventhub.event_management.entities.RegistrationEntity;
import com.eventhub.event_management.enums.EventStatus;
import com.eventhub.event_management.enums.Role;
import com.eventhub.event_management.exceptions.CustomBadRequestException;
import com.eventhub.event_management.repositories.EventRepository;
import com.eventhub.event_management.repositories.RegistrationEventRepository;
import com.eventhub.event_management.security.jwt.JwtTokenManager;
import com.eventhub.event_management.services.converter.EventEntityMapper;
import com.eventhub.event_management.vo.Event;
import com.eventhub.event_management.vo.Location;
import com.eventhub.event_management.vo.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.eventhub.event_management.services.EventSpecification.greaterThanOrEqualToPlace;
import static com.eventhub.event_management.services.EventSpecification.hasName;


@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;
    private final RegistrationEventRepository registrationEventRepository;
    private final UserService userService;
    private final LocationService locationService;
    private final EventEntityMapper eventEntityMapper;
    private final JwtTokenManager jwtTokenManager;

    public EventService(EventRepository eventRepository, RegistrationEventRepository registrationEventRepository,
                        UserService userService, LocationService locationService,
                        EventEntityMapper eventEntityMapper, JwtTokenManager jwtTokenManager) {
        this.eventRepository = eventRepository;
        this.registrationEventRepository = registrationEventRepository;
        this.userService = userService;
        this.locationService = locationService;
        this.eventEntityMapper = eventEntityMapper;
        this.jwtTokenManager = jwtTokenManager;
    }

    @Transactional
    public EventDTO createEvent(HttpServletRequest request, EventRequestDto eventRequestDto) {
        User user = getLoginFromRequest(request);
        Location location = locationService.getLocationById(eventRequestDto.locationId());

        if (eventRequestDto.maxPlaces().compareTo(location.capacity()) > 0) {
            throw new IllegalArgumentException("The location cannot accommodate such a number of participants.");
        }

        EventEntity eventEntity = eventEntityMapper.toCreateEventEntityFromRequest(eventRequestDto,
                user.id(),
                location.id());

        eventRepository.save(eventEntity);
        return eventEntityMapper.toEventDTO(eventEntity);
    }

    @Transactional
    public void deleteEvent(HttpServletRequest request, Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.error("Event with id {} does not exist.", eventId);
            throw new EntityNotFoundException("Event not found.");
        }

        User user = getLoginFromRequest(request);
        Event event = getEventById(eventId);

        if (!isAdminOrOwner(user, event)) {
            throw new IllegalArgumentException("There are no rights to cancel the event.");
        }

        if (Objects.equals(EventStatus.STARTED, event.status())) {
            throw new CustomBadRequestException("The event cannot be deleted. Event has been started.");
        }

        if (Objects.equals(EventStatus.FINISHED, event.status())) {
            throw new CustomBadRequestException("The event cannot be deleted. Event has been finished.");
        }

        if (Objects.equals(user.id(), event.ownerId()) && Objects.equals(EventStatus.WAIT_START, event.status())) {
            EventEntity eventEntity = eventEntityMapper.toEventEntity(event);
            eventEntity.setStatus(EventStatus.CANCELLED);
            eventRepository.save(eventEntity);
        }
    }

    @Transactional
    public EventDTO updateEvent(HttpServletRequest request, Long eventId, EventRequestDto eventRequestDto) {
        if (!eventRepository.existsById(eventId)) {
            log.error("Event with id {} does not exist.", eventId);
            throw new EntityNotFoundException("Event not found.");
        }

        User user = getLoginFromRequest(request);
        Event event = getEventById(eventId);
        Location location = locationService.getLocationById(eventRequestDto.locationId());

        if (!isAdminOrOwner(user, event)) {
            throw new IllegalArgumentException("There are no rights to cancel the event.");
        }

        if (Objects.equals(EventStatus.STARTED, event.status())) {
            throw new CustomBadRequestException("The event cannot be updated. Event has been started.");
        }

        if (eventRequestDto.maxPlaces().compareTo(location.capacity()) > 0) {
            throw new CustomBadRequestException("The location cannot accommodate such a number of participants.");
        }

        if (event.occupiedPlaces().compareTo(eventRequestDto.maxPlaces()) > 0) {
            throw new CustomBadRequestException("More participants have already registered for the event.");
        }

        EventEntity eventEntity = eventEntityMapper.toUpdateEventEntityFromRequest(eventRequestDto, event);

        eventRepository.save(eventEntity);
        return eventEntityMapper.toEventDTO(eventEntity);
    }

    @Transactional
    public void registrationEvent(HttpServletRequest request, Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.error("Event with id {} does not exist.", eventId);
            throw new EntityNotFoundException("Event not found.");
        }
        User user = getLoginFromRequest(request);
        Event event = getEventById(eventId);

        if (Objects.equals(EventStatus.WAIT_START, event.status()) &&
                !Objects.equals(event.occupiedPlaces(), event.maxPlaces())) {
            RegistrationEntity registrationEntity = new RegistrationEntity();
            registrationEntity.setUserId(user.id());
            registrationEntity.setEvent(eventEntityMapper.toEventEntity(event));
            registrationEventRepository.save(registrationEntity);
        } else {
            throw new IllegalArgumentException("The event cannot be registered.");
        }
    }

    public List<EventDTO> getMyRegistrationEvents(HttpServletRequest request) {
        User user = getLoginFromRequest(request);

        List<RegistrationEntity> registrationEntities = registrationEventRepository.findAllByUserId(user.id());

        if (registrationEntities.isEmpty()) {
            return Collections.emptyList();
        }

        Set<EventEntity> events = registrationEntities.stream()
                .map(RegistrationEntity::getEvent)
                .collect(Collectors.toSet());

        return events.stream()
                .map(eventEntityMapper::toEventDTO)
                .toList();
    }

    @Transactional
    public void deleteRegistrationEvent(HttpServletRequest request, Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.error("Event with id {} does not exist.", eventId);
            throw new EntityNotFoundException("Event not found.");
        }
        User user = getLoginFromRequest(request);
        Event event = getEventById(eventId);

        if (Objects.equals(EventStatus.WAIT_START, event.status())) {
            List<RegistrationEntity> registrationEntities = registrationEventRepository.findAllByUserId(user.id());

            Long registrationId = registrationEntities.stream()
                    .filter(registrationUser -> Objects.equals(user.id(), registrationUser.getUserId()))
                    .filter(registrationEvent -> Objects.equals(registrationEvent.getEvent().getId(), event.id()))
                    .map(RegistrationEntity::getId)
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Registration not found."));
            registrationEventRepository.deleteById(registrationId);
        } else {
            throw new IllegalArgumentException("The event cannot be deleted.");
        }
    }

    public List<EventDTO> getAllEvents(EventSearchRequestDto eventSearchRequestDto) {
        Specification<EventEntity> specification = hasName(eventSearchRequestDto.name())
                .and(greaterThanOrEqualToPlace(eventSearchRequestDto.placesMin()))
                .and(EventSpecification.lessThanOrEqualToPlace(eventSearchRequestDto.placesMax()))
                .and(EventSpecification.equalsDateStartAfter(eventSearchRequestDto.dateStartAfter()))
                .and(EventSpecification.equalsDateStartBefore(eventSearchRequestDto.dateStartBefore()))
                .and(EventSpecification.greaterThanOrEqualToCost(eventSearchRequestDto.costMin()))
                .and(EventSpecification.lessThanOrEqualToCost(eventSearchRequestDto.costMax()))
                .and(EventSpecification.greaterThanOrEqualToDuration(eventSearchRequestDto.durationMin()))
                .and(EventSpecification.lessThanOrEqualToDuration(eventSearchRequestDto.durationMax()))
                .and(EventSpecification.hasLocationId(eventSearchRequestDto.locationId()))
                .and(EventSpecification.hasEventStatus(eventSearchRequestDto.eventStatus()));
        return eventRepository.findAll(specification).stream()
                .map(eventEntityMapper::toEventDTO)
                .toList();
    }

    public List<EventEntity> findAllEvents() {
        return eventRepository.findAll();
    }

    public List<EventDTO> getMyEvents(HttpServletRequest request) {
        User user = getLoginFromRequest(request);
        List<EventEntity> listAllEvents = eventRepository.findAll().stream()
                .filter(item -> Objects.equals(user.id(), item.getOwnerId()))
                .toList();
        return listAllEvents.stream()
                .map(eventEntityMapper::toEventDTO)
                .toList();
    }

    public Event getEventById(Long eventId) {
        EventEntity foundEventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("No event found with id: " + eventId));
        return eventEntityMapper.toEvent(foundEventEntity);
    }

    private LocalDateTime stringToLocalDateTime(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private User getLoginFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String login = jwtTokenManager.getLoginFromToken(bearerToken.substring(7));
        return userService.findByLogin(login);
    }

    private boolean isAdminOrOwner(User user, Event event) {
        boolean isAdmin = Objects.equals(user.role(), Role.ADMIN);
        boolean isOwner = Objects.equals(user.id(), event.ownerId());

        return isAdmin || isOwner;
    }
}
