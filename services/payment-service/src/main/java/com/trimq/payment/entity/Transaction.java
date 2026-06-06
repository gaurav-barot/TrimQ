package com.trimq.payment.entity;

import com.trimq.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Transaction entity - Detailed transaction log.
 * 
 * Each payment may have multiple transactions (create, attempt, success/failure, refund).
 */
@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "idx_txn_payment_id", columnList = "paymentId"),
    @Index(name = "idx_txn_shop_id", columnList = "shopId"),
    @Index(name = "idx_txn_type", columnList = "type"),
    @Index(name = "idx_txn_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Associated payment
     */
    @Column(nullable = false)
    private Long paymentId;

    /**
     * Shop ID (for shop owner queries)
     */
    @Column(nullable = false)
    private Long shopId;

    /**
     * Transaction type
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType type;

    /**
     * Transaction amount
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * Currency
     */
    @Column(nullable = false, length = 3)
    @Builder.Default
    private String currency = "INR";

    /**
     * Transaction status
     */
    @Column(nullable = false, length = 20)
    private String status;

    /**
     * External reference (Razorpay ID)
     */
    @Column(length = 50)
    private String externalRef;

    /**
     * Description
     */
    @Column(length = 500)
    private String description;

    /**
     * Raw response from payment gateway (JSON)
     */
    @Column(columnDefinition = "TEXT")
    private String rawResponse;

    /**
     * Transaction types
     */
    public enum TransactionType {
        ORDER_CREATED,    // Initial order creation
        PAYMENT_ATTEMPT,  // Payment attempt initiated
        PAYMENT_SUCCESS,  // Payment successful
        PAYMENT_FAILED,   // Payment failed
        REFUND_INITIATED, // Refund initiated
        REFUND_SUCCESS,   // Refund successful
        REFUND_FAILED     // Refund failed
    }
}

