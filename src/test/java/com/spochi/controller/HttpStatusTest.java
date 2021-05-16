package com.spochi.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpStatusTest {

    @Test
    void getCode() {
        assertEquals(800, HttpStatus.BAD_REQUEST.getCode());
        assertEquals(801, HttpStatus.NICKNAME_ALREADY_TAKEN.getCode());
    }
}