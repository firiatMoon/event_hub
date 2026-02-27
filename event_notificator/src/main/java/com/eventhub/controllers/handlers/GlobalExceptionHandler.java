package com.eventhub.controllers.handlers;


import com.eventhub.exceptions.CustomBadRequestException;
import com.eventhub.exceptions.ErrorMessageResponse;
import com.eventhub.services.LocaleMessageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final LocaleMessageService messageService;

    public GlobalExceptionHandler(LocaleMessageService messageService) {
        this.messageService = messageService;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleNotFoundException(EntityNotFoundException ex) {

        ErrorMessageResponse error = new ErrorMessageResponse(
                messageService.getMessage("{error.title.entity.not-found}"),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorMessageResponse> handleNoSuchElementException(NoSuchElementException ex) {

        ErrorMessageResponse error = new ErrorMessageResponse(
                messageService.getMessage("error.title.element.not-found"),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidationException(MethodArgumentNotValidException ex) {

        String detailedMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ErrorMessageResponse error = new ErrorMessageResponse(
                messageService.getMessage("{error.title.validation-error}"),
                detailedMessage,
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessageResponse> handleException(Exception ex) {
        ErrorMessageResponse error = new ErrorMessageResponse(
                messageService.getMessage("{error.title.server-error}"),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<ErrorMessageResponse> handlerBadRequestException(CustomBadRequestException ex) {
        ErrorMessageResponse error = new ErrorMessageResponse(
                messageService.getMessage("{error.title.bad-request}"),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
