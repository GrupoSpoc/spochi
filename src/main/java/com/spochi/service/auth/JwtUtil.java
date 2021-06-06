package com.spochi.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.time.DateUtils;
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
    public String generateAdminToken(String uid) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("admin", true);
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

    /**
     * Claims es un mapa clave valor con los distintos datos encriptados en el jwt
     * Tiene claves fijas.
     * En nuestro caso guardamos la expiración como EXPIRATION y el uid como SUBJECT
     */
    protected Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token).getBody();
    }

    /**
     * Le sumamos un minuto a la fecha actual,
     * ya que luego de validar el JWT otros servicios lo consumen.
     * Entonces nos aseguramos que sea válido ahora y de acá a un minuto en el futuro.
     */
    private boolean isTokenExpired(String token) {
        final Date now = new Date(currentMillis());
        return extractExpiration(token).before(DateUtils.addMinutes(now, 1));
    }

    protected String getSecretKey() {
        return secretKey;
    }

    protected long currentMillis() {
        return System.currentTimeMillis();
    }
}

