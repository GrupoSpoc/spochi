package com.spochi.service;

import com.spochi.controller.exception.BadRequestException;
import com.spochi.controller.exception.HttpStatus;

public class UserServiceException extends BadRequestException {
    public UserServiceException(String message) {
        super(message);
    }

    public UserServiceException(String message, HttpStatus status) {
        super(message, status);
    }
}
