package com.eventhub.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "telegram_user")
public class TelegramUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "chat_id", nullable = false, unique = true)
    private Long chatId;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public TelegramUser() {
    }

    public TelegramUser(Long id, Long userId, Long chatId) {
        this.id = id;
        this.userId = userId;
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        return String.format("TelegramUser{userId = %d, chatId = %d}");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TelegramUser other = (TelegramUser) obj;
        return Objects.equals(userId, other.userId) &&
                Objects.equals(chatId, other.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, chatId);
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

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}
