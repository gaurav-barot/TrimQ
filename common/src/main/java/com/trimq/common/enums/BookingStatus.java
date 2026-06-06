package com.trimq.common.enums;

/**
 * Booking lifecycle status.
 */
public enum BookingStatus {
    PENDING,        // Booking created, awaiting payment
    CONFIRMED,      // Payment successful, booking confirmed
    CHECKED_IN,     // Customer checked in at shop
    IN_PROGRESS,    // Service in progress
    COMPLETED,      // Service completed
    CANCELLED,      // Booking cancelled by user
    NO_SHOW,        // Customer didn't show up
    RESCHEDULED     // Booking rescheduled to new slot
}

