package com.trimq.common.enums;

/**
 * Time slot availability status.
 */
public enum SlotStatus {
    AVAILABLE,      // Slot is available for booking
    LOCKED,         // Slot is temporarily locked (during payment)
    BOOKED,         // Slot is booked
    BLOCKED         // Slot is blocked by shop owner
}

