package com.rzk.api_gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {
    private static final String SECRET = "coffee-shop-super-secret-key-min-32-characters-long!";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public Claims validateAndGetClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
