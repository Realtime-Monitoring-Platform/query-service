package com.realtime_monitoring.query_service.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class ErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private String details;

    
    public ErrorResponse(LocalDateTime timestamp, String message, int status) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = "HTTP Status Code: " + status;
    }
}
