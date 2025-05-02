package com.example.security.advices;

import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
@Data
public class APIError {
    private HttpStatusCode statusCode;
    private LocalDateTime dateTime;
    private String error;

    public APIError() {
        this.dateTime = LocalDateTime.now();
    }

    public APIError(HttpStatusCode statusCode, String error) {
        this();
        this.statusCode = statusCode;
        this.error = error;
    }
}
