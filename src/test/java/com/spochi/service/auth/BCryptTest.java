package com.spochi.service.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("BCrypt usage test tutorial")
public class BCryptTest {
    
    @Test
    void sampleTest() {
        final String passwordPlainText = "1234";

        // encripto texto plano usando BCrypt
        final String passwordEncoded = BCrypt.hashpw(passwordPlainText, BCrypt.gensalt());

        // comparo el texto plano contra el password encoded usando BCrypt
        assertTrue(BCrypt.checkpw(passwordPlainText, passwordEncoded));
    }
}
