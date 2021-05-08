package com.spochi.service.authenticate;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class JwtUtil {
    @Value("${jwt.secret.key}")
    private String secretKey;

    public String generateToken(String uid) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, uid);
    }

    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    public String extractUid(String token) {
        return extractAllClaims(token).getSubject();
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(currentMillis() + TimeUnit.HOURS.toMillis(3)))
                .signWith(SignatureAlgorithm.HS256, getSecretKey()).compact();
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date(currentMillis()));
    }

    protected String getSecretKey() {
        return secretKey;
    }

    protected long currentMillis() {
        return System.currentTimeMillis();
    }
}

