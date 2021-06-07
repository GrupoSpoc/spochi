package com.spochi.controller.exception;

import com.spochi.controller.HttpStatus;

public class AdminAuthorizationException extends RuntimeException{
    private final HttpStatus status;
    public AdminAuthorizationException() {
        super();
        this.status = HttpStatus.BAD_ADMIN_REQUEST;
    }

}
