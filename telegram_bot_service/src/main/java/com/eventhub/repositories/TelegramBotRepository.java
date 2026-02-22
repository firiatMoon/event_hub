package com.eventhub.repositories;

import com.eventhub.entities.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramBotRepository extends JpaRepository<TelegramUser, Long> {
    boolean existsByChatId(Long chatId);

    void deleteByChatId(Long chatId);

    TelegramUser findByChatId(Long chatId);

    Optional<TelegramUser> findByUserId(Long userId);
}
