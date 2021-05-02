package com.spochi.entity;

import com.spochi.controller.handler.BadRequestException;
import lombok.Getter;

import java.util.Arrays;

public enum InitiativeStatus {
    PENDING(1),
    APPROVED(2),
    REJECTED(3);


    @Getter private final int id;

    InitiativeStatus(int id) {
        this.id = id;
    }

    public static InitiativeStatus fromIdOrElseThrow(int id) {
        return Arrays.stream(values())
                .filter(v -> v.id == id)
                .findFirst()
                .orElseThrow(() -> new InitiativeStatusNotFoundException(id));
    }

    public static class InitiativeStatusNotFoundException extends BadRequestException {
        private InitiativeStatusNotFoundException(int id) {
            super(String.format("No InitiativeStatus with id [%s] present", id));
        }
    }
}
