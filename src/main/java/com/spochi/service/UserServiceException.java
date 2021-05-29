package com.spochi.service;

import com.spochi.controller.exception.BadRequestException;
import com.spochi.controller.HttpStatus;

public class UserServiceException extends BadRequestException {
    public UserServiceException(String failField) {
        super(String.format("The Services fail because : %s", failField));
    }

    public UserServiceException(String message, HttpStatus status) {

        super(String.format("The Services fail because : %s", message), status);
    }
}
