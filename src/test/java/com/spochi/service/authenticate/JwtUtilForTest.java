package com.spochi.service.authenticate;

public class JwtUtilForTest extends JwtUtil {
    @Override
    protected String getSecretKey() {
        return "secret";
    }
}
