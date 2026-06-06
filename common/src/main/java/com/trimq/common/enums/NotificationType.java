package com.trimq.common.enums;

/**
 * Types of notifications sent by the platform.
 */
public enum NotificationType {
    // Booking notifications
    BOOKING_CONFIRMATION,
    BOOKING_REMINDER,
    BOOKING_CANCELLATION,
    BOOKING_RESCHEDULED,
    
    // Payment notifications
    PAYMENT_SUCCESS,
    PAYMENT_FAILED,
    REFUND_PROCESSED,
    
    // User notifications
    OTP_VERIFICATION,
    PASSWORD_RESET,
    WELCOME,
    
    // Shop notifications
    NEW_BOOKING,
    BOOKING_CANCELLED_SHOP,
    DAILY_SUMMARY
}

