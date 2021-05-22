package com.spochi.controller;

import lombok.Getter;

@Getter
public enum HttpStatus {
    BAD_REQUEST(800),
    NICKNAME_ALREADY_TAKEN(801);

    private final int code;

    HttpStatus(int code) {
        this.code = code;
    }
}