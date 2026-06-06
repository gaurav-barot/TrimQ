package com.trimq.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * TrimQ Booking Service - Handles slot management and bookings.
 * 
 * Responsibilities:
 * - Time slot generation and availability
 * - Slot locking (Redis)
 * - Booking CRUD
 * - Pass code generation
 * - QR code generation
 * - Pass validation
 * - Kafka event publishing
 */
@SpringBootApplication(scanBasePackages = {"com.trimq.booking", "com.trimq.common"})
@EnableJpaAuditing
@EnableScheduling
public class BookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingServiceApplication.class, args);
    }
}

