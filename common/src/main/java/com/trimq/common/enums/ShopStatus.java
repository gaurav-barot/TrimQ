package com.trimq.common.enums;

/**
 * Shop registration and operational status.
 */
public enum ShopStatus {
    PENDING_APPROVAL,   // Awaiting admin approval
    ACTIVE,             // Shop is active and accepting bookings
    INACTIVE,           // Shop is temporarily inactive
    SUSPENDED,          // Shop suspended by admin
    CLOSED              // Shop permanently closed
}

