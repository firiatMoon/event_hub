package com.eventhub.controllers;

import com.eventhub.services.TelegramCodeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/telegram")
public class TelegramCodeController {


    private final TelegramCodeService telegramCodeService;

    public TelegramCodeController(TelegramCodeService telegramCodeService) {
        this.telegramCodeService = telegramCodeService;
    }

    @Operation(summary = "Генерация ссылки для привязки Telegram-бота",
    description = "Создает временный код в Redis для привязки аккаунта. Ссылка действительна 10 минут.")
    @GetMapping("/link")
    public ResponseEntity<String> generateTelegramLink(){
        Long currentUserId = (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getDetails();
        String message = telegramCodeService.generateTelegramCode(currentUserId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(message);
    }

    @Operation(summary = "Проверка кода привязки Telegram",
    description = "Принимает временный код из бота, возвращает ID пользователя и удаляет код из Redis")
    @GetMapping("/verify/{code}")
    public ResponseEntity<Long> verifyCode(@PathVariable String code) {
        Long userId = telegramCodeService.verifyTelegramCode(code);
        if (Objects.isNull(userId)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userId);
    }
}
