package com.trimq.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * DTO for Razorpay webhook payload.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookPayload {

    private String event;

    @JsonProperty("account_id")
    private String accountId;

    private boolean contains;

    @JsonProperty("created_at")
    private Long createdAt;

    private Map<String, Object> payload;

    /**
     * Get the payment entity from payload
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getPaymentEntity() {
        if (payload != null && payload.containsKey("payment")) {
            Map<String, Object> payment = (Map<String, Object>) payload.get("payment");
            if (payment.containsKey("entity")) {
                return (Map<String, Object>) payment.get("entity");
            }
        }
        return null;
    }

    /**
     * Get the order entity from payload
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getOrderEntity() {
        if (payload != null && payload.containsKey("order")) {
            Map<String, Object> order = (Map<String, Object>) payload.get("order");
            if (order.containsKey("entity")) {
                return (Map<String, Object>) order.get("entity");
            }
        }
        return null;
    }

    /**
     * Get the refund entity from payload
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getRefundEntity() {
        if (payload != null && payload.containsKey("refund")) {
            Map<String, Object> refund = (Map<String, Object>) payload.get("refund");
            if (refund.containsKey("entity")) {
                return (Map<String, Object>) refund.get("entity");
            }
        }
        return null;
    }
}

