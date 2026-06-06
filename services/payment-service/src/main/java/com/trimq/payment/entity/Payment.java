package com.trimq.payment.entity;

import com.trimq.common.entity.BaseEntity;
import com.trimq.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Payment entity - Stores payment order information.
 */
@Entity
@Table(name = "payments", indexes = {
    @Index(name = "idx_payment_booking_id", columnList = "bookingId"),
    @Index(name = "idx_payment_user_id", columnList = "userId"),
    @Index(name = "idx_payment_shop_id", columnList = "shopId"),
    @Index(name = "idx_payment_order_id", columnList = "razorpayOrderId"),
    @Index(name = "idx_payment_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Associated booking ID
     */
    @Column(nullable = false)
    private Long bookingId;

    /**
     * User making the payment
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * Shop receiving the payment
     */
    @Column(nullable = false)
    private Long shopId;

    /**
     * Payment amount
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * Currency (INR)
     */
    @Column(nullable = false, length = 3)
    @Builder.Default
    private String currency = "INR";

    /**
     * Razorpay Order ID (order_xxxxx)
     */
    @Column(unique = true, length = 50)
    private String razorpayOrderId;

    /**
     * Razorpay Payment ID (pay_xxxxx)
     */
    @Column(length = 50)
    private String razorpayPaymentId;

    /**
     * Razorpay Signature for verification
     */
    @Column(length = 200)
    private String razorpaySignature;

    /**
     * Internal receipt number (TQ-XXXX)
     */
    @Column(unique = true, length = 30)
    private String receipt;

    /**
     * Payment status
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    /**
     * Payment method (upi, card, netbanking, wallet)
     */
    @Column(length = 30)
    private String paymentMethod;

    /**
     * Bank name (for netbanking)
     */
    @Column(length = 100)
    private String bank;

    /**
     * Wallet name (if wallet payment)
     */
    @Column(length = 50)
    private String wallet;

    /**
     * VPA (UPI ID) if UPI payment
     */
    @Column(length = 100)
    private String vpa;

    /**
     * Card last 4 digits (if card payment)
     */
    @Column(length = 4)
    private String cardLast4;

    /**
     * Card network (visa, mastercard, etc.)
     */
    @Column(length = 20)
    private String cardNetwork;

    /**
     * Failure reason (if failed)
     */
    @Column(length = 500)
    private String failureReason;

    /**
     * Razorpay error code (if failed)
     */
    @Column(length = 50)
    private String errorCode;

    /**
     * Refund ID (if refunded)
     */
    @Column(length = 50)
    private String refundId;

    /**
     * Refund amount (partial or full)
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal refundAmount;

    /**
     * Customer email (for receipt)
     */
    @Column(length = 100)
    private String customerEmail;

    /**
     * Customer phone
     */
    @Column(length = 15)
    private String customerPhone;

    /**
     * Customer name
     */
    @Column(length = 100)
    private String customerName;

    /**
     * Notes/description
     */
    @Column(length = 500)
    private String description;

    /**
     * Check if payment can be refunded
     */
    public boolean isRefundable() {
        return status == PaymentStatus.SUCCESS && refundId == null;
    }
}

