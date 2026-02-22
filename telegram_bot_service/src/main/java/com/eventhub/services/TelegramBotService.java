package com.eventhub.services;

import com.eventhub.entities.TelegramUser;
import com.eventhub.enums.EventStatus;
import com.eventhub.enums.RegistrationResult;
import com.eventhub.kafka.events.EventChangeKafkaMessage;
import com.eventhub.repositories.TelegramBotRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class TelegramBotService {

    private static final Logger log = LoggerFactory.getLogger(TelegramBotService.class);

    private final TelegramBotRepository telegramBotRepository;
    private final MessageSender messageSender;

    public TelegramBotService(TelegramBotRepository telegramBotRepository, MessageSender messageSender) {
        this.telegramBotRepository = telegramBotRepository;
        this.messageSender = messageSender;
    }

    @Transactional
    public RegistrationResult saveTelegramUser(Long userId, Long chatId) {
        if (telegramBotRepository.existsById(userId)) {
            return RegistrationResult.ALREADY_EXISTS;
        }

        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setUserId(userId);
        telegramUser.setChatId(chatId);
        telegramBotRepository.save(telegramUser);
        return RegistrationResult.SUCCESS;
    }

    public boolean isChatLinked(Long chatId) {
        return telegramBotRepository.existsByChatId(chatId);
    }

    public Long getUserIdByChatId(Long chatId) {
        return telegramBotRepository.findByChatId(chatId).getUserId();
    }

    public Optional<Long> getChatIdByUserId(Long userId) {
        return telegramBotRepository.findByUserId(userId)
                .map(TelegramUser::getChatId);
    }

    @Transactional
    public void deleteTelegramUser(Long chatId) {
        if (!telegramBotRepository.existsByChatId(chatId)) {
            log.error("User with chatId {} does not exist.", chatId);
            throw new EntityNotFoundException("User not found.");
        }
        telegramBotRepository.deleteByChatId(chatId);
    }

    public void notificationOfChanges(EventChangeKafkaMessage event) {
        String notificationText = buildChangeMessage(event);

        event.getSubscribersToEvent().forEach(userId -> {
            getChatIdByUserId(userId).ifPresent(chatId -> {
                messageSender.sendMessage(chatId, notificationText);
            });
        });
    }

    private String buildChangeMessage(EventChangeKafkaMessage event) {
        StringBuilder notificationMessage = new StringBuilder("Attention! Changes in: \n\n");

        if (!Objects.isNull(event.getName())) {
            notificationMessage.append("Name: \n")
                    .append("\told: ").append(event.getName().getOldField())
                    .append("\n\tnew: ").append(event.getName().getNewField())
                    .append("\n");
        }

        if (!Objects.isNull(event.getDate())) {
            notificationMessage.append("Date: \n")
                    .append("\told: ").append(event.getDate().getOldField().toLocalDate())
                    .append("\n\tnew: ").append(event.getDate().getNewField().toLocalDate())
                    .append("\n");

            notificationMessage.append("Time: \n")
                    .append("\told: ").append(event.getDate().getOldField().toLocalTime())
                    .append("\n\tnew: ").append(event.getDate().getNewField().toLocalTime())
                    .append("\n");
        }

        if (!Objects.isNull(event.getCost())) {
            notificationMessage.append("Cost: \n")
                    .append("\told: ").append(event.getCost().getOldField())
                    .append("\n\tnew: ").append(event.getCost().getNewField())
                    .append("\n");
        }

        if (!Objects.isNull(event.getLocationId())) {
            notificationMessage.append("Location: \n")
                    .append("\told: ").append(event.getLocationId().getOldField())
                    .append("\n\tnew: ").append(event.getLocationId().getNewField())
                    .append("\n");
        }

        if (!Objects.isNull(event.getStatus())) {
            notificationMessage.append("Status: \n")
                    .append("\told: ").append(event.getStatus().getOldField())
                    .append("\n\tnew: ").append(EventStatus.valueOf(event.getStatus().getNewField()))
                    .append("\n");
        }
        return notificationMessage.toString();
    }
}
