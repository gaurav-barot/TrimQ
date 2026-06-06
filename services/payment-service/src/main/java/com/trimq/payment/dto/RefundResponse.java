package com.trimq.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for refund operation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundResponse {

    private Long paymentId;
    private String refundId;
    private BigDecimal refundAmount;
    private String status;
    private String speed;
    private String reason;
    private LocalDateTime createdAt;
    private String message;
}

