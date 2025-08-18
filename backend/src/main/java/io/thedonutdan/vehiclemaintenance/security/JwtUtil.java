package io.thedonutdan.vehiclemaintenance.security;

import java.time.Duration;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final String keyFP = Base64.getEncoder().encodeToString(key.getEncoded()).substring(0, 12);

    public String generateToken(UUID userId, Duration expiry) {
        String t = Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiry.toMillis()))
            .signWith(key)
            .compact();
        System.out.println("[JwtUtil] issue sub=" + userId + " fp=" + keyFP + " token=" + snip(t));
        return t;
    }

    public UUID parseToken(String token) {
        System.out.println("[JwtUtil] parse  fp=" + keyFP + " token=" + snip(token));
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

        return UUID.fromString(claims.getSubject()); // Just the username
    }

    public String getFingerprint() {
        return keyFP;
    }

    private static String snip(String token) {
        if (token == null) return "null";
        int n = token.length();
        return (n <= 16) ? token : (token.substring(0, 8) + "..." + token.substring(n - 8));
    }
}
