package com.eventhub.services.clients;

import com.eventhub.dto.EventTelegramBotDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "event-manager")
public interface EventManagerClient {

    @GetMapping("/telegram/verify/{code}")
    Long verifyCode(@PathVariable("code") String code);

    @GetMapping("/internal/telegram/user/{userId}/events")
    List<EventTelegramBotDTO> getEvents(
            @PathVariable("userId") Long userId,
            @RequestParam("onlyToday") boolean onlyToday
    );
}
