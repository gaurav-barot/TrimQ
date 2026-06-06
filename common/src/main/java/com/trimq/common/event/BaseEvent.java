package com.trimq.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base event class for all Kafka events.
 * Provides common fields for event tracking and tracing.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEvent {

    private String eventId;
    private String eventType;
    private LocalDateTime timestamp;
    private String source;
    private String traceId;

    protected void initializeEvent(String eventType, String source) {
        this.eventId = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now();
        this.source = source;
        if (this.traceId == null) {
            this.traceId = UUID.randomUUID().toString();
        }
    }
}

