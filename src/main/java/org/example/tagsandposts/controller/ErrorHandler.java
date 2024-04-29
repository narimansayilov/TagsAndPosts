package org.example.tagsandposts.controller;

import jakarta.validation.ConstraintViolationException;
import org.example.tagsandposts.model.exception.ExceptionResponse;
import org.example.tagsandposts.model.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ExceptionResponse handle(NotFoundException exception){
        log.error("NotFoundException ", exception);
        return new ExceptionResponse(exception.getCode());
    }

    @ExceptionHandler(Exception.class)
    ExceptionResponse handler(Exception exception){
        log.error("Exception ", exception);
        return new ExceptionResponse("UNEXPECETED_EXCEPTION");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handle(ConstraintViolationException exception){
        log.error("ConstraintViolationException ", exception);
        List<ExceptionResponse> errors = new ArrayList<>();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                 HttpHeaders headers,
                                                                 HttpStatusCode status,
                                                                 WebRequest request){
        log.error("Validation exception: ", exception);
        List<ExceptionResponse> errors = new ArrayList<>();
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(e -> errors.add(new ExceptionResponse(e.getDefaultMessage())));
        return ResponseEntity.status(status).body(errors);
    }
}
