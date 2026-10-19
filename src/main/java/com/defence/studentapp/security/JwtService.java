package com.defence.studentapp.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "my-super-secret-key-for-student-app-jwt-2026";

    private final SecretKey secretKey;

    public JwtService() {
        this.secretKey = Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(String username, String role) {

        Instant now = Instant.now();

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(java.util.Date.from(now))
                .expiration(
                        java.util.Date.from(
                                now.plus(5, ChronoUnit.MINUTES)
                        )
                )
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String extractRole(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }
}