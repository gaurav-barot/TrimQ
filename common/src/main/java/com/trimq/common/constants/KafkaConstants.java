package com.trimq.common.constants;

/**
 * Kafka-related constants for event-driven communication.
 */
public final class KafkaConstants {

    private KafkaConstants() {
        // Prevent instantiation
    }

    // ==================== TOPICS ====================
    public static final String TOPIC_BOOKING_EVENTS = "booking-events";
    public static final String TOPIC_PAYMENT_EVENTS = "payment-events";
    public static final String TOPIC_NOTIFICATION_EVENTS = "notification-events";

    // ==================== CONSUMER GROUPS ====================
    public static final String GROUP_NOTIFICATION_SERVICE = "notification-service-group";
    public static final String GROUP_ANALYTICS_SERVICE = "analytics-service-group";

    // ==================== EVENT TYPES ====================
    // Booking Events
    public static final String EVENT_BOOKING_CREATED = "BOOKING_CREATED";
    public static final String EVENT_BOOKING_CONFIRMED = "BOOKING_CONFIRMED";
    public static final String EVENT_BOOKING_CANCELLED = "BOOKING_CANCELLED";
    public static final String EVENT_BOOKING_COMPLETED = "BOOKING_COMPLETED";
    public static final String EVENT_BOOKING_NO_SHOW = "BOOKING_NO_SHOW";
    public static final String EVENT_BOOKING_RESCHEDULED = "BOOKING_RESCHEDULED";

    // Payment Events
    public static final String EVENT_PAYMENT_INITIATED = "PAYMENT_INITIATED";
    public static final String EVENT_PAYMENT_SUCCESS = "PAYMENT_SUCCESS";
    public static final String EVENT_PAYMENT_FAILED = "PAYMENT_FAILED";
    public static final String EVENT_REFUND_INITIATED = "REFUND_INITIATED";
    public static final String EVENT_REFUND_PROCESSED = "REFUND_PROCESSED";
}

