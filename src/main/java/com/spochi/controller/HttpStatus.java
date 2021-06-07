package com.spochi.controller;

import lombok.Getter;

@Getter
public enum HttpStatus {
    BAD_REQUEST(800),
    NICKNAME_ALREADY_TAKEN(801),
    INITIATIVE_NOT_FOUND(802),
    BAD_INITIATIVE_STATUS(803),
    BAD_ADMIN_REQUEST(805),
    FIWARE_ERROR(900);

    private final int code;

    HttpStatus(int code) {
        this.code = code;
    }
}