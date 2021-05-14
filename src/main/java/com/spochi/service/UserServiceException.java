package com.spochi.service;

import com.spochi.controller.exception.BadRequestException;

public class UserServiceException extends BadRequestException {
    public UserServiceException(String message) {
        super(message);
    }
}
