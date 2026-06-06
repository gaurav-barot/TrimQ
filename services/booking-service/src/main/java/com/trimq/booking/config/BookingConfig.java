package com.trimq.booking.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for booking service.
 */
@Configuration
@ConfigurationProperties(prefix = "booking")
@Data
public class BookingConfig {
    
    /**
     * Slot lock TTL in seconds (default 5 minutes)
     */
    private int slotLockTtlSeconds = 300;
    
    /**
     * Maximum days in advance to book
     */
    private int maxAdvanceDays = 7;
    
    /**
     * Minimum hours in advance to book
     */
    private int minAdvanceHours = 1;
    
    /**
     * Default slot duration in minutes
     */
    private int defaultSlotDurationMinutes = 30;
}

