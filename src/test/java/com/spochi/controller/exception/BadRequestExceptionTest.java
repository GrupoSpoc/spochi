package com.spochi.controller.exception;

import com.spochi.controller.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BadRequestExceptionTest {

    @Test
    @DisplayName("get status | when built without status")
    void getStatusBuiltWithoutStatus() {
        final BadRequestException exception = new BadRequestException("error");

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("error", exception.getMessage());
    }

    @Test
    @DisplayName("get status | when built with status")
    void getStatusWithStatus() {
        final BadRequestException exception = new BadRequestException("error-nickname", HttpStatus.NICKNAME_ALREADY_TAKEN);

        assertEquals(HttpStatus.NICKNAME_ALREADY_TAKEN, exception.getStatus());
        assertEquals("error-nickname", exception.getMessage());
    }
}