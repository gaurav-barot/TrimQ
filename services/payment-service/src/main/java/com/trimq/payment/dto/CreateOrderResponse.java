package com.trimq.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response DTO for Razorpay order creation.
 * Contains all details needed for frontend to initiate payment.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResponse {

    private Long paymentId;
    
    // Razorpay details
    private String razorpayOrderId;
    private String razorpayKeyId;
    private BigDecimal amount;
    private Long amountInPaise;
    private String currency;
    private String receipt;
    
    // Customer details
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    
    // Booking details
    private Long bookingId;
    private String description;
    
    // Callback URLs
    private String callbackUrl;
    private String cancelUrl;
    
    // Notes for Razorpay
    private String notes;
}

