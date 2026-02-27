package com.eventhub.services;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;

@Service
public class LocaleMessageService {
    private final MessageSource messageSource;

    public LocaleMessageService(MessageSource messageSource1) {
        this.messageSource = messageSource1;
    }

    public String getMessage(String code, String language) {
        Locale locale = !Objects.isNull(language)
                ? Locale.forLanguageTag(language)
                : Locale.of("en", "US");
        return messageSource.getMessage(code, null, locale);
    }

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}
