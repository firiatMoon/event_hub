package com.eventhub.services;

import com.eventhub.dto.EventDTO;
import com.eventhub.dto.EventRequestDto;
import com.eventhub.dto.EventSearchRequestDto;
import com.eventhub.dto.EventTelegramBotDTO;
import com.eventhub.entities.EventEntity;
import com.eventhub.entities.RegistrationEntity;
import com.eventhub.enums.EventStatus;
import com.eventhub.enums.Role;
import com.eventhub.exceptions.CustomBadRequestException;
import com.eventhub.kafka.KafkaProducer;
import com.eventhub.kafka.events.EventChangeKafkaMessage;
import com.eventhub.kafka.events.EventFieldChange;
import com.eventhub.repositories.EventRepository;
import com.eventhub.repositories.RegistrationEventRepository;
import com.eventhub.repositories.UserRepository;
import com.eventhub.services.converter.EventEntityMapper;
import com.eventhub.model.Event;
import com.eventhub.model.Location;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.eventhub.services.EventSpecification.greaterThanOrEqualToPlace;
import static com.eventhub.services.EventSpecification.hasName;


@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);
    private static final String UNKNOWN_LOCATION = "Unknown Location";

    private final EventRepository eventRepository;
    private final RegistrationEventRepository registrationEventRepository;
    private final LocationService locationService;
    private final EventEntityMapper eventEntityMapper;
    private final KafkaProducer kafkaProducer;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository, RegistrationEventRepository registrationEventRepository,
                        LocationService locationService, EventEntityMapper eventEntityMapper,
                        KafkaProducer kafkaProducer, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.registrationEventRepository = registrationEventRepository;
        this.locationService = locationService;
        this.eventEntityMapper = eventEntityMapper;
        this.kafkaProducer = kafkaProducer;
        this.userRepository = userRepository;
    }

    @Transactional
    public EventDTO createEvent(EventRequestDto eventRequestDto) {

        Long currentUserId = (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getDetails();
        Location location = locationService.getLocationById(eventRequestDto.locationId());

        if (eventRequestDto.maxPlaces().compareTo(location.capacity()) > 0) {
            throw new IllegalArgumentException("The location cannot accommodate such a number of participants.");
        }

        EventEntity eventEntity = eventEntityMapper.toCreateEventEntityFromRequest(eventRequestDto,
                currentUserId,
                location.id());

        eventRepository.save(eventEntity);
        return eventEntityMapper.toEventDTO(eventEntity);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.error("Event with id {} does not exist.", eventId);
            throw new EntityNotFoundException("Event not found.");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = (Long) auth.getDetails();
        String role = auth.getAuthorities().iterator().next().getAuthority();

        Event event = getEventById(eventId);

        if (!isAdminOrOwner(currentUserId, role, event)) {
            throw new IllegalArgumentException("There are no rights to cancel the event.");
        }

        if (Objects.equals(EventStatus.STARTED, event.status())) {
            throw new CustomBadRequestException("The event cannot be deleted. Event has been started.");
        }

        if (Objects.equals(EventStatus.FINISHED, event.status())) {
            throw new CustomBadRequestException("The event cannot be deleted. Event has been finished.");
        }

        if (Objects.equals(currentUserId, event.ownerId()) && Objects.equals(EventStatus.WAIT_START, event.status())) {
            EventEntity eventEntity = eventEntityMapper.toEventEntity(event);
            eventEntity.setStatus(EventStatus.CANCELLED);
            eventRepository.save(eventEntity);
        }
    }

    @Transactional
    public EventDTO updateEvent(Long eventId, EventRequestDto eventRequestDto) {
        if (!eventRepository.existsById(eventId)) {
            log.error("Event with id {} does not exist.", eventId);
            throw new EntityNotFoundException("Event not found.");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = (Long) auth.getDetails();
        String role = auth.getAuthorities().iterator().next().getAuthority();

        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found."));

        Location location = locationService.getLocationById(eventRequestDto.locationId());

        if (!isAdminOrOwner(currentUserId, role, eventEntityMapper.toEvent(eventEntity))) {
            throw new IllegalArgumentException("There are no rights to cancel the event.");
        }

        if (Objects.equals(EventStatus.STARTED, eventEntityMapper.toEvent(eventEntity).status())) {
            throw new CustomBadRequestException("The event cannot be updated. Event has been started.");
        }

        if (eventRequestDto.maxPlaces().compareTo(location.capacity()) > 0) {
            throw new CustomBadRequestException("The location cannot accommodate such a number of participants.");
        }

        if (eventEntityMapper.toEvent(eventEntity).occupiedPlaces().compareTo(eventRequestDto.maxPlaces()) > 0) {
            throw new CustomBadRequestException("More participants have already registered for the event.");
        }

        EventChangeKafkaMessage message = createEventChangeKafkaMessage(currentUserId,
                eventEntityMapper.toEvent(eventEntity), eventRequestDto);

        eventEntityMapper.updateEntityFromRequest(eventRequestDto, eventEntity);
        eventRepository.save(eventEntity);

        kafkaProducer.send(message);
        log.info("Successful send: {}, idUserChanged: {}", message.toString(), message.getIdUserChanged());
        return eventEntityMapper.toEventDTO(eventEntity);
    }

    @Transactional
    public void registrationEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.error("Event with id {} does not exist.", eventId);
            throw new EntityNotFoundException("Event not found.");
        }

        Long currentUserId = (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getDetails();

        Event event = getEventById(eventId);

        List<RegistrationEntity> registrationForEvent = registrationEventRepository
                .findAllByUserId(currentUserId)
                .stream()
                .filter(item -> Objects.equals(item.getEvent().getId(), event.id()))
                .toList();

        if(!registrationForEvent.isEmpty()) {
            throw new CustomBadRequestException("You are already registered for this event.");
        }

        if (Objects.equals(EventStatus.WAIT_START, event.status()) &&
                !Objects.equals(event.occupiedPlaces(), event.maxPlaces())) {
            RegistrationEntity registrationEntity = new RegistrationEntity();
            registrationEntity.setUserId(currentUserId);
            registrationEntity.setEvent(eventEntityMapper.toEventEntity(event));
            registrationEventRepository.save(registrationEntity);
        } else {
            throw new IllegalArgumentException("The event cannot be registered.");
        }
    }

    public List<EventDTO> getMyRegistrationEvents() {
        Long currentUserId = (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getDetails();

        List<RegistrationEntity> registrationEntities = registrationEventRepository.
                findAllByUserId(currentUserId);

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

    public List<EventTelegramBotDTO> getEventsByUserId(Long userId, boolean onlyToday) {
        if (!userRepository.existsById(userId)) {
            throw new UsernameNotFoundException("User not found with id: " + userId);
        }

        List<RegistrationEntity> registrationEntities = registrationEventRepository.findAllByUserId(userId);

        if (registrationEntities.isEmpty()) {
            return Collections.emptyList();
        }

        Stream<EventTelegramBotDTO> events = registrationEntities.stream()
                .map(RegistrationEntity::getEvent)
                .map(event -> {
                    String location = UNKNOWN_LOCATION;
                    if(!Objects.isNull(locationService.getLocationById(event.getId()))) {
                        location = locationService.getLocationById(event.getId()).name();
                    } else {
                        throw new EntityNotFoundException("Location not found.");
                    }
                    return eventEntityMapper.toEventTelegramBotDTO(event, location);
                });

        if (onlyToday) {
            events = events
                    .filter(event -> Objects.equals(event.date().toLocalDate(), LocalDate.now()));
        }
        return events.toList();
    }

    @Transactional
    public void deleteRegistrationEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.error("Event with id {} does not exist.", eventId);
            throw new EntityNotFoundException("Event not found.");
        }
        Long currentUserId = (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getDetails();
        Event event = getEventById(eventId);

        if (Objects.equals(EventStatus.WAIT_START, event.status())) {
            List<RegistrationEntity> registrationEntities = registrationEventRepository.findAllByUserId(currentUserId);

            Long registrationId = registrationEntities.stream()
                    .filter(registrationUser -> Objects.equals(currentUserId, registrationUser.getUserId()))
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

    @Transactional
    public List<EventDTO> getMyEvents() {
        Long currentUserId = (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getDetails();

        List<EventEntity> listAllEvents = eventRepository.findAll().stream()
                .filter(item -> Objects.equals(currentUserId, item.getOwnerId()))
                .toList();
        return listAllEvents.stream()
                .map(eventEntityMapper::toEventDTO)
                .toList();
    }

    public Event getEventById(Long eventId) {
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("No event found with id: " + eventId));
        return eventEntityMapper.toEvent(eventEntity);
    }

    private boolean isAdminOrOwner(Long userId, String role, Event event) {
        boolean isAdmin = Objects.equals(role, Role.ADMIN.toString());
        boolean isOwner = Objects.equals(userId, event.ownerId());

        return isAdmin || isOwner;
    }

    private EventChangeKafkaMessage createEventChangeKafkaMessage(Long userID,
                                                                  Event event,
                                                                  EventRequestDto eventRequestDto) {
        EventChangeKafkaMessage eventChangeKafkaMessage = new EventChangeKafkaMessage();

        eventChangeKafkaMessage.setEventId(event.id());
        eventChangeKafkaMessage.setIdUserChanged(!Objects.isNull(userID) ? userID : 0);
        eventChangeKafkaMessage.setOwnerId(event.ownerId());

        List<Long> registrationUsers = registrationEventRepository.findUsersRegisteredForEvent(event.id());

        eventChangeKafkaMessage.setSubscribersToEvent(registrationUsers);

        if(!Objects.equals(event.name(), eventRequestDto.name())) {
            eventChangeKafkaMessage.setName(new EventFieldChange<String>(event.name(), eventRequestDto.name()));
        }

        if(!Objects.equals(event.maxPlaces(), eventRequestDto.maxPlaces())) {
            eventChangeKafkaMessage.setMaxPlaces(new EventFieldChange<Integer>(event.maxPlaces(), eventRequestDto.maxPlaces()));
        }

        LocalDateTime requestedDate = LocalDateTime.parse(eventRequestDto.date());
        if(!Objects.equals(event.date(), requestedDate)) {
            eventChangeKafkaMessage.setDate(new EventFieldChange<LocalDateTime>(event.date(), requestedDate));
        }

        if(event.cost().compareTo(eventRequestDto.cost()) != 0) {
            BigDecimal oldCost = event.cost().setScale(2, RoundingMode.HALF_UP);
            BigDecimal newCost = eventRequestDto.cost().setScale(2, RoundingMode.HALF_UP);
            eventChangeKafkaMessage.setCost(new EventFieldChange<BigDecimal>(oldCost, newCost));
        }

        if(!Objects.equals(event.duration(), eventRequestDto.duration())) {
            eventChangeKafkaMessage.setDuration(new EventFieldChange<Integer>(event.duration(), eventRequestDto.duration()));
        }

        if(!Objects.equals(event.locationId(), eventRequestDto.locationId())) {
            eventChangeKafkaMessage.setLocationId(new EventFieldChange<Long>(event.locationId(), eventRequestDto.locationId()));
        }

        if(eventRequestDto.status() != null && !Objects.equals(event.status(), eventRequestDto.status())) {
            eventChangeKafkaMessage.setStatus(new EventFieldChange<String>(event.status().toString(),
                    eventRequestDto.status().toString()));
        }

        return eventChangeKafkaMessage;
    }
}
