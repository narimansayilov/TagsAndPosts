package org.example.tagsandposts.model.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class ExceptionResponse {

    private final String message;
    private final String code;

    public ExceptionResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }
}