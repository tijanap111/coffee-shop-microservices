package com.rzk.api_gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {
    private static final String SECRET =
            "coffee_shop_super_secret_key_min_32_characters_long!!!";
    private final SecretKey key =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    private static final Logger log = LoggerFactory.getLogger(JwtGatewayFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        HttpMethod method = request.getMethod();

        if (method == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        // Public endpoints (bez tokena)
        if (path.contains("/users/register") || path.contains("/users/login")) {
            return chain.filter(exchange);
        }

        // Public read-only (GET) endpoints - meni je javan svima
        if (method == HttpMethod.GET && isPublicGet(path)) {
            return chain.filter(exchange);
        }

        final String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("Missing/invalid Authorization header for path={}", path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        final String token = authHeader.substring(7).trim();

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String email = claims.getSubject();
            String role = claims.get("role", String.class);
            email = (email == null) ? "" : email.trim();
            role = (role == null) ? "" : role.trim();

            log.debug("JWT OK path={} email={} role={}", path, email, role);

            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Email", email)
                    .header("X-User-Role", role)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception e) {
            log.debug("JWT INVALID path={} msg={}", path, e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private boolean isPublicGet(String path) {
        return path.contains("/categories") || path.contains("/products") || path.contains("/ingredients");
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
