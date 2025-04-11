package com.smartinvent.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {
    private String errorId;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    private Map<String, Object> details;

    public ErrorResponse(String errorId, String message, LocalDateTime timestamp, String path, Map<String, Object> details) {
        this.errorId = errorId;
        this.message = message;
        this.timestamp = timestamp;
        this.path = path;
        this.details = details;
    }

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }
}
