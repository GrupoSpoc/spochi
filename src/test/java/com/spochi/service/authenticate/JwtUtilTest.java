package com.spochi.service.authenticate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @Test
    void generateToken() {
        assertNotNull(new JwtUtil().generateToken("token"));
    }
}