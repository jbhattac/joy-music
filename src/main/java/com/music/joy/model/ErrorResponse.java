package com.music.joy.model;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class ErrorResponse {

    private HttpStatus status;
    private String debugMessage;
    private Map<String, String> violationErrors = new HashMap<>();

    public ErrorResponse(HttpStatus status, String debugMessage) {
        super();
        this.debugMessage = debugMessage;
        this.status = status;
    }

    public void addViolationErrors(String property, String message) {
        violationErrors.put(property, message);
    }
}
