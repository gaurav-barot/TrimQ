package com.trimq.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Fallback controller for circuit breaker.
 * Returns friendly error messages when services are unavailable.
 */
@Slf4j
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/user-service")
    public Mono<ResponseEntity<Map<String, Object>>> userServiceFallback() {
        log.warn("User service fallback triggered");
        return createFallbackResponse("User service is temporarily unavailable");
    }

    @GetMapping("/shop-service")
    public Mono<ResponseEntity<Map<String, Object>>> shopServiceFallback() {
        log.warn("Shop service fallback triggered");
        return createFallbackResponse("Shop service is temporarily unavailable");
    }

    @GetMapping("/booking-service")
    public Mono<ResponseEntity<Map<String, Object>>> bookingServiceFallback() {
        log.warn("Booking service fallback triggered");
        return createFallbackResponse("Booking service is temporarily unavailable");
    }

    @GetMapping("/payment-service")
    public Mono<ResponseEntity<Map<String, Object>>> paymentServiceFallback() {
        log.warn("Payment service fallback triggered");
        return createFallbackResponse("Payment service is temporarily unavailable");
    }

    /**
     * Create a standard fallback response.
     */
    private Mono<ResponseEntity<Map<String, Object>>> createFallbackResponse(String message) {
        Map<String, Object> response = Map.of(
                "success", false,
                "message", message,
                "error", Map.of(
                        "code", "SERVICE_UNAVAILABLE",
                        "message", message
                ),
                "timestamp", LocalDateTime.now().toString()
        );
        
        return Mono.just(ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response));
    }
}

