package com.spochi.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpStatusTest {

    @Test
    void getCode() {
        assertEquals(800, HttpStatus.BAD_REQUEST.getCode());
        assertEquals(801, HttpStatus.NICKNAME_ALREADY_TAKEN.getCode());
        assertEquals(900, HttpStatus.FIWARE_ERROR.getCode());
        assertEquals(805, HttpStatus.BAD_ADMIN_REQUEST.getCode());
    }
}