package com.smartinvent.config;

import com.smartinvent.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        String errorId = UUID.randomUUID().toString();

        logger.error("❌ Error ID: {} | Message: {} | Path: {}", errorId, ex.getMessage(), request.getDescription(false), ex);

        Map<String, Object> details = new HashMap<>();
        details.put("requestPath", request.getDescription(false));

        ErrorResponse response = new ErrorResponse(
                errorId,
                "An unexpected error occurred",
                LocalDateTime.now(),
                request.getDescription(false),
                Collections.emptyMap()
        );


        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        String errorId = UUID.randomUUID().toString();

        logger.warn("⚠️ IllegalArgument | ID: {} | Message: {}", errorId, ex.getMessage());

        Map<String, Object> details = new HashMap<>();
        details.put("requestPath", request.getDescription(false));

        ErrorResponse response = new ErrorResponse(
                errorId,
                ex.getMessage(),
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
