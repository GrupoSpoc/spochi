package com.spochi.controller.exception;

import com.spochi.controller.HttpStatus;

public class BadRequestException extends RuntimeException {
    private final HttpStatus status;

    public BadRequestException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
