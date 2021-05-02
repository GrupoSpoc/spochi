package com.spochi.entity;

import com.spochi.controller.handler.BadRequestException;

import java.util.Arrays;

public enum UserType {
    PERSON(1),
    ORGANIZATION(2),
    ADMIN(3);

    private final int id;

    UserType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static UserType fromIdOrElseThrow(int id) {
        return Arrays.stream(values())
                .filter(v -> v.id == id)
                .findFirst()
                .orElseThrow(() -> new UserTypeNotFoundException(id));
    }

    public static class UserTypeNotFoundException extends BadRequestException {
        private UserTypeNotFoundException(int id) {
            super(String.format("No UserType with id [%s] present", id));
        }
    }
}
