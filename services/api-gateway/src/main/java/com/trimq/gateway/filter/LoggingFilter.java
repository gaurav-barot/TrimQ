package com.trimq.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Global logging filter for request/response tracking.
 * Adds trace ID for distributed tracing.
 */
@Slf4j
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    private static final String START_TIME_ATTR = "startTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        // Generate or extract trace ID
        String traceId = request.getHeaders().getFirst(TRACE_ID_HEADER);
        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString().substring(0, 8);
        }
        
        // Add trace ID to request headers for downstream services
        ServerHttpRequest mutatedRequest = request.mutate()
                .header(TRACE_ID_HEADER, traceId)
                .build();
        
        // Store start time for duration calculation
        long startTime = System.currentTimeMillis();
        
        // Log incoming request
        log.info("[{}] --> {} {} from {}",
                traceId,
                request.getMethod(),
                request.getPath(),
                request.getRemoteAddress() != null ? request.getRemoteAddress().getAddress().getHostAddress() : "unknown"
        );

        String finalTraceId = traceId;
        return chain.filter(exchange.mutate().request(mutatedRequest).build())
                .then(Mono.fromRunnable(() -> {
                    ServerHttpResponse response = exchange.getResponse();
                    long duration = System.currentTimeMillis() - startTime;
                    
                    // Add trace ID to response headers
                    response.getHeaders().add(TRACE_ID_HEADER, finalTraceId);
                    
                    // Log response
                    log.info("[{}] <-- {} {} - {} ({}ms)",
                            finalTraceId,
                            request.getMethod(),
                            request.getPath(),
                            response.getStatusCode(),
                            duration
                    );
                    
                    // Warn for slow requests
                    if (duration > 2000) {
                        log.warn("[{}] Slow request detected: {} {} took {}ms",
                                finalTraceId,
                                request.getMethod(),
                                request.getPath(),
                                duration
                        );
                    }
                }));
    }

    @Override
    public int getOrder() {
        // Execute first (before other filters)
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

