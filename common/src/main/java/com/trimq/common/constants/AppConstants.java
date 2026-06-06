package com.trimq.common.constants;

/**
 * Application-wide constants.
 */
public final class AppConstants {

    private AppConstants() {
        // Prevent instantiation
    }

    // ==================== API PATHS ====================
    public static final String API_V1 = "/api/v1";
    public static final String AUTH_PATH = "/auth";
    public static final String USERS_PATH = "/users";
    public static final String SHOPS_PATH = "/shops";
    public static final String SERVICES_PATH = "/services";
    public static final String STAFF_PATH = "/staff";
    public static final String BOOKINGS_PATH = "/bookings";
    public static final String SLOTS_PATH = "/slots";
    public static final String PAYMENTS_PATH = "/payments";

    // ==================== PAGINATION ====================
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_BY = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION = "desc";

    // ==================== CACHE KEYS ====================
    public static final String CACHE_OTP_PREFIX = "otp:";
    public static final String CACHE_SLOT_LOCK_PREFIX = "slot:lock:";
    public static final String CACHE_SESSION_PREFIX = "session:";
    public static final String CACHE_RATE_LIMIT_PREFIX = "rate:";
    public static final String CACHE_SHOP_PREFIX = "shop:";

    // ==================== TTL (seconds) ====================
    public static final long OTP_TTL_SECONDS = 300;        // 5 minutes
    public static final long SLOT_LOCK_TTL_SECONDS = 300;  // 5 minutes
    public static final long SESSION_TTL_SECONDS = 86400;  // 24 hours
    public static final long RATE_LIMIT_TTL_SECONDS = 60;  // 1 minute
    public static final long SHOP_CACHE_TTL_SECONDS = 3600; // 1 hour

    // ==================== KAFKA TOPICS ====================
    public static final String TOPIC_BOOKING_EVENTS = "booking-events";
    public static final String TOPIC_PAYMENT_EVENTS = "payment-events";
    public static final String TOPIC_NOTIFICATION_EVENTS = "notification-events";

    // ==================== BOOKING PASS ====================
    public static final String PASS_PREFIX = "TQ";
    public static final int PASS_CODE_LENGTH = 8;

    // ==================== SLOT CONFIGURATION ====================
    public static final int DEFAULT_SLOT_DURATION_MINUTES = 30;
    public static final int MAX_ADVANCE_BOOKING_DAYS = 7;
    public static final int MIN_ADVANCE_BOOKING_HOURS = 1;

    // ==================== BUSINESS RULES ====================
    public static final int FREE_CANCELLATION_HOURS = 2;
    public static final double LATE_CANCELLATION_CHARGE_PERCENT = 50.0;
    public static final double PLATFORM_COMMISSION_PERCENT = 5.0;
    public static final double PAYMENT_GATEWAY_FEE_PERCENT = 2.0;
}

