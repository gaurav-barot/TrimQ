package com.trimq.payment.dto;

import com.trimq.payment.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for transaction details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private Long id;
    private Long paymentId;
    private Long shopId;
    private Transaction.TransactionType type;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String externalRef;
    private String description;
    private LocalDateTime createdAt;
}

