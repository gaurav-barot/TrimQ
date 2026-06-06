package com.trimq.gateway.filter;

import com.trimq.gateway.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * JWT Authentication Filter for API Gateway.
 * Validates JWT tokens and enriches requests with user information.
 * 
 * This filter:
 * 1. Extracts JWT from Authorization header
 * 2. Validates the token
 * 3. Adds user info to request headers for downstream services
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    private final JwtUtil jwtUtil;

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USER_ROLE = "X-User-Role";
    private static final String HEADER_USER_EMAIL = "X-User-Email";

    public JwtAuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            // Check for Authorization header
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                log.warn("Missing Authorization header for path: {}", request.getPath());
                return onError(exchange, "Missing Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            
            // Validate Bearer token format
            if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
                log.warn("Invalid Authorization header format");
                return onError(exchange, "Invalid Authorization header format", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(BEARER_PREFIX.length());

            // Validate token
            if (!jwtUtil.validateToken(token)) {
                log.warn("Invalid or expired JWT token");
                return onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
            }

            // Extract user information and add to headers
            try {
                String userId = jwtUtil.extractUserId(token);
                String role = jwtUtil.extractRole(token);
                String email = jwtUtil.extractEmail(token);

                log.debug("Authenticated user: {} with role: {}", userId, role);

                // Mutate request to add user info headers for downstream services
                ServerHttpRequest mutatedRequest = request.mutate()
                        .header(HEADER_USER_ID, userId)
                        .header(HEADER_USER_ROLE, role)
                        .header(HEADER_USER_EMAIL, email != null ? email : "")
                        .build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build());
                
            } catch (Exception e) {
                log.error("Error extracting claims from token: {}", e.getMessage());
                return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
            }
        };
    }

    /**
     * Handle error response.
     */
    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        
        String body = String.format(
            "{\"success\":false,\"message\":\"%s\",\"error\":{\"code\":\"UNAUTHORIZED\",\"message\":\"%s\"}}",
            message, message
        );
        
        return exchange.getResponse().writeWith(
            Mono.just(exchange.getResponse().bufferFactory().wrap(body.getBytes()))
        );
    }

    public static class Config {
        // Configuration properties if needed
    }
}

