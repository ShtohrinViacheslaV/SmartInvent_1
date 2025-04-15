package com.smartinvent.config;

import com.smartinvent.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageSource messageSource;
    private final SentryErrorHandler sentryErrorHandler;


    public GlobalExceptionHandler(MessageSource messageSource, SentryErrorHandler sentryErrorHandler) {
        this.messageSource = messageSource;
        this.sentryErrorHandler = sentryErrorHandler;

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request, Locale locale) {
        String errorId = UUID.randomUUID().toString();

        sentryErrorHandler.logError(ex);

        String message = messageSource.getMessage("error.login.server", null, locale);

        logger.error("❌ Error ID: {} | Message: {} | Path: {}", errorId, ex.getMessage(), request.getDescription(false), ex);

        Map<String, Object> details = new HashMap<>();
        details.put("requestPath", request.getDescription(false));

        ErrorResponse response = new ErrorResponse(
                errorId,
                message,
                LocalDateTime.now(),
                request.getDescription(false),
                Collections.emptyMap()
        );


        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, WebRequest request, Locale locale) {
        String errorId = UUID.randomUUID().toString();
        String message = messageSource.getMessage("error.employee.not.found", null, locale);

        sentryErrorHandler.logError(ex);

        logger.warn("⚠️ IllegalArgument | ID: {} | Message: {}", errorId, ex.getMessage());

        Map<String, Object> details = new HashMap<>();
        details.put("requestPath", request.getDescription(false));

        ErrorResponse response = new ErrorResponse(
                errorId,
                message,
                LocalDateTime.now(),
                request.getDescription(false),
                Collections.emptyMap()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        String errorId = UUID.randomUUID().toString();

        logger.error("Error ID: {} - Message: {} - Description: {}", errorId, ex.getMessage(), request.getDescription(false));

        Map<String, Object> details = new HashMap<>();
        details.put("requestPath", request.getDescription(false));

        ErrorResponse response = new ErrorResponse(
                errorId,
                ex.getMessage(),
                LocalDateTime.now(),
                request.getDescription(false),
                Collections.emptyMap()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
