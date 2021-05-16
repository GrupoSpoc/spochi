package com.spochi.controller.exception;

public class BadRequestException extends RuntimeException {
    private final int statusCode;

    public BadRequestException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String message, HttpStatus status) {
        super(message);
        this.statusCode = status.getCode();
    }

    public int getStatusCode() {
        return statusCode;
    }
}
