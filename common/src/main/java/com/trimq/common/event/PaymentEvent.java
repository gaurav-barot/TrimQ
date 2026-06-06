package com.trimq.common.event;

import com.trimq.common.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Event published when a payment is processed.
 * 
 * Used for async communication with notification-service.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaymentEvent extends BaseEvent {

    private Long paymentId;
    private Long bookingId;
    private Long userId;
    private Long shopId;
    
    // Customer details for notifications
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    
    // Payment details
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String paymentMethod;
    
    // Refund details (if applicable)
    private String refundId;
    private BigDecimal refundAmount;
    private String refundReason;
    
    // Failure details
    private String failureReason;

    /**
     * Factory method to create a payment event.
     */
    public static PaymentEvent create(String eventType, Long paymentId, Long bookingId) {
        PaymentEvent event = new PaymentEvent();
        event.initializeEvent(eventType, "payment-service");
        event.setPaymentId(paymentId);
        event.setBookingId(bookingId);
        return event;
    }
}
