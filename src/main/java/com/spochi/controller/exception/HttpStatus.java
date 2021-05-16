package com.spochi.controller.exception;

import lombok.Getter;

@Getter
public enum HttpStatus {
    BAD_REQUEST(400),
    NICKNAME_ALREADY_TAKEN(800);

    private final int code;

    HttpStatus(int code) {
        this.code = code;
    }
}
