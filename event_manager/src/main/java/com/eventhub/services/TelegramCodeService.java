package com.eventhub.services;

import com.eventhub.model.TelegramCodeSession;
import com.eventhub.repositories.RedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class TelegramCodeService {

    private final static Logger log = LoggerFactory.getLogger(TelegramCodeService.class);
    private final RedisRepository redisRepository;

    @Value("${telegram.bot.name}")
    private String botName;

    public TelegramCodeService(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    public String generateTelegramCode(Long userId) {
        String code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));

        TelegramCodeSession session = new TelegramCodeSession(code, userId, 600L);
        redisRepository.save(session);
        log.info("Binding code {} created for user {}", code, userId);

        String url = "https://t.me/" + botName;
        return String.format("1. Open the bot using the link: %s%n2. Send him this verification code: %s",
                url, code
        );
    }

    public Long verifyTelegramCode(String code) {
        return redisRepository.findById(code)
                .map(session -> {
                    Long userId = session.getUserId();
                    redisRepository.delete(session);
                    return userId;
                })
                .orElseGet(() -> {
                    log.warn("Code {} not found in Redis (possibly final TTL)", code);
                    return null;
                });
    }
}
