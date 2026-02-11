package org.thedonutdan.autolog.security;

import java.time.Duration;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(UUID userId, Duration expiry) {
        String t = Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiry.toMillis()))
            .signWith(key)
            .compact();
        return t;
    }

    public UUID parseToken(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

        return UUID.fromString(claims.getSubject()); // Just the username
    }
}
