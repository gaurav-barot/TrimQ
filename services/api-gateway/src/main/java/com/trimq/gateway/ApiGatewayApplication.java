package com.trimq.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TrimQ API Gateway - Entry point for all API requests.
 * 
 * Responsibilities:
 * - Route requests to appropriate microservices
 * - JWT token validation
 * - Rate limiting
 * - Circuit breaker for resilience
 * - Request/Response logging
 * - Metrics export for Prometheus
 */
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}

