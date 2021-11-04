package com.example.filesharing.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class BasicExceptionPayload {

    private String message;
    private HttpStatus httpStatus;
    private LocalDateTime localDateTime;

    public BasicExceptionPayload(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.localDateTime = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
}
