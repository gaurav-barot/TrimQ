package com.trimq.common.enums;

/**
 * Payment transaction status.
 */
public enum PaymentStatus {
    PENDING,        // Payment initiated
    PROCESSING,     // Payment being processed
    SUCCESS,        // Payment successful
    FAILED,         // Payment failed
    REFUND_PENDING, // Refund requested
    REFUNDED,       // Refund completed
    PARTIALLY_REFUNDED  // Partial refund completed
}

