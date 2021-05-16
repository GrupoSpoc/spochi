package com.spochi.controller.handler;

import com.spochi.controller.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {BadRequestException.class})
    protected ResponseEntity<String> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.status(e.getStatus().getCode())
                .body(e.getMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}