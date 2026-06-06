package com.trimq.payment.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for initiating a refund.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequest {

    @NotNull(message = "Payment ID is required")
    private Long paymentId;

    @DecimalMin(value = "0.01", message = "Refund amount must be positive")
    private BigDecimal amount; // Optional: null means full refund

    @NotBlank(message = "Refund reason is required")
    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    private String reason;

    /**
     * Speed: "normal" (5-7 days) or "optimum" (instant, if eligible)
     */
    private String speed;
}

