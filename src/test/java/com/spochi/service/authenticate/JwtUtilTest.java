package com.spochi.service.authenticate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("disable-firebase")
class JwtUtilTest {
    @Test
    void generateToken() {
        assertNotNull(new JwtUtilForTest().generateToken("token"));
    }
}