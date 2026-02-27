package com.eventhub.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("tg_code")
public class TelegramCodeSession {
    @Id
    private String code;
    private Long userId;

    @TimeToLive
    private Long expirationInSeconds;

    public TelegramCodeSession() {
    }

    public TelegramCodeSession(String code, Long userId, Long expirationInSeconds) {
        this.code = code;
        this.userId = userId;
        this.expirationInSeconds = expirationInSeconds;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getExpirationInSeconds() {
        return expirationInSeconds;
    }

    public void setExpirationInSeconds(Long expirationInSeconds) {
        this.expirationInSeconds = expirationInSeconds;
    }
}
