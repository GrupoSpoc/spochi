package com.spochi.repository.fiware.rest;

import lombok.Getter;

public class RestException extends RuntimeException {
    @Getter private Integer code;

    public RestException(String message) {
        super(message);
    }


    public RestException(String message, Integer code) {
        super(message);
        this.code = code;
    }
}
