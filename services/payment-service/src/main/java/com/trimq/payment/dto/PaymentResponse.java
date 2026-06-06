package com.trimq.payment.dto;

import com.trimq.common.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for payment details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private Long bookingId;
    private Long userId;
    private Long shopId;
    
    // Amount
    private BigDecimal amount;
    private String currency;
    
    // Razorpay IDs
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String receipt;
    
    // Status
    private PaymentStatus status;
    private String statusDescription;
    
    // Payment method details
    private String paymentMethod;
    private String bank;
    private String wallet;
    private String vpa;
    private String cardLast4;
    private String cardNetwork;
    
    // Customer
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    
    // Refund
    private String refundId;
    private BigDecimal refundAmount;
    private boolean refundable;
    
    // Failure
    private String failureReason;
    private String errorCode;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

