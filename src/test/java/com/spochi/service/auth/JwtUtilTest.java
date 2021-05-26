package com.spochi.service.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("disable-firebase")
class JwtUtilTest {

    @Test
    @DisplayName("generate token | ok")
    void generateToken() {
        assertNotNull(new JwtUtilForTest().generateToken("uid"));
    }

    @Test
    @DisplayName("extract uid | ok")
    void extractUid() {
        final JwtUtilForTest jwtUtil = new JwtUtilForTest();
        final String jwt = jwtUtil.generateToken("uid");

        assertEquals("uid", jwtUtil.extractUid(jwt));
    }

    @Test
    @DisplayName("is token valid | when token is expired | should return false")
    void tokenExpiredAfterThreeHours() {
        final JwtUtilForTest jwtUtilNow = new JwtUtilForTest();
        final String jwt = jwtUtilNow.generateToken("uid");
        final long millisAfterThreeHoursAndOneMinute = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(3L) + TimeUnit.MINUTES.toMillis(1L);
        final JwtUtilForTest jwtUtilAfterThreeHoursAndOneMinute = new JwtUtilForTest(() -> millisAfterThreeHoursAndOneMinute);

        assertFalse(jwtUtilAfterThreeHoursAndOneMinute.isTokenValid(jwt));
    }

    @Test
    @DisplayName("is token valid | when token expires in a minute from now | should return false")
    void tokenExpiresInAMinute() {
        final JwtUtilForTest jwtUtilNow = new JwtUtilForTest();
        final String jwt = jwtUtilNow.generateToken("uid");
        final long millisAfterThreeHoursAndOneMinute = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(3L) - TimeUnit.MINUTES.toMillis(1L);
        final JwtUtilForTest jwtUtilAfterThreeHoursAndOneMinute = new JwtUtilForTest(() -> millisAfterThreeHoursAndOneMinute);

        assertFalse(jwtUtilAfterThreeHoursAndOneMinute.isTokenValid(jwt));
    }

    @Test
    @DisplayName("is token valid | when token expires in two minutes from now | should return true")
    void tokenExpiresInTwoMinutesOk() {
        final JwtUtilForTest jwtUtilNow = new JwtUtilForTest();
        final String jwt = jwtUtilNow.generateToken("uid");
        final long millisAfterThreeHoursAndOneMinute = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(3L) - TimeUnit.MINUTES.toMillis(2L);
        final JwtUtilForTest jwtUtilAfterThreeHoursAndOneMinute = new JwtUtilForTest(() -> millisAfterThreeHoursAndOneMinute);

        assertTrue(jwtUtilAfterThreeHoursAndOneMinute.isTokenValid(jwt));
    }

    @Test
    @DisplayName("is token valid | when token is far from being expired | should return true")
    void tokenNotExpiredAfterTwoHours() {
        final JwtUtilForTest jwtUtilNow = new JwtUtilForTest();
        final String jwt = jwtUtilNow.generateToken("uid");
        final long millisAfterTwoHours = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(2L);
        final JwtUtilForTest jwtUtilAfterThreeHoursAndOneMinute = new JwtUtilForTest(() -> millisAfterTwoHours);

        assertTrue(jwtUtilAfterThreeHoursAndOneMinute.isTokenValid(jwt));
    }
}