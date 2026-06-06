package com.trimq.notification.consumer;

import com.trimq.common.constants.KafkaConstants;
import com.trimq.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Kafka consumer for payment events.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = KafkaConstants.TOPIC_PAYMENT_EVENTS, groupId = "notification-service")
    public void handlePaymentEvent(Map<String, Object> event) {
        String eventType = getStringValue(event, "eventType");
        log.info("Received payment event: type={}", eventType);

        try {
            switch (eventType) {
                case KafkaConstants.EVENT_PAYMENT_SUCCESS:
                    handlePaymentSuccess(event);
                    break;
                case KafkaConstants.EVENT_PAYMENT_FAILED:
                    handlePaymentFailed(event);
                    break;
                case KafkaConstants.EVENT_REFUND_PROCESSED:
                    handleRefundProcessed(event);
                    break;
                default:
                    log.debug("Unhandled payment event type: {}", eventType);
            }
        } catch (Exception e) {
            log.error("Error processing payment event: {}", eventType, e);
        }
    }

    private void handlePaymentSuccess(Map<String, Object> event) {
        Long paymentId = getLongValue(event, "paymentId");
        Long bookingId = getLongValue(event, "bookingId");
        Long userId = getLongValue(event, "userId");
        String customerName = getStringValue(event, "customerName");
        String customerEmail = getStringValue(event, "customerEmail");
        String customerPhone = getStringValue(event, "customerPhone");
        BigDecimal amount = getBigDecimal(event, "amount");
        String razorpayPaymentId = getStringValue(event, "razorpayPaymentId");

        notificationService.sendPaymentSuccess(
                paymentId, bookingId, userId,
                customerName, customerEmail, customerPhone,
                amount, razorpayPaymentId);
    }

    private void handlePaymentFailed(Map<String, Object> event) {
        Long paymentId = getLongValue(event, "paymentId");
        Long bookingId = getLongValue(event, "bookingId");
        Long userId = getLongValue(event, "userId");
        String customerEmail = getStringValue(event, "customerEmail");
        String customerPhone = getStringValue(event, "customerPhone");
        String failureReason = getStringValue(event, "failureReason");

        notificationService.sendPaymentFailed(
                paymentId, bookingId, userId,
                customerEmail, customerPhone, failureReason);
    }

    private void handleRefundProcessed(Map<String, Object> event) {
        Long paymentId = getLongValue(event, "paymentId");
        Long bookingId = getLongValue(event, "bookingId");
        Long userId = getLongValue(event, "userId");
        String customerName = getStringValue(event, "customerName");
        String customerEmail = getStringValue(event, "customerEmail");
        String customerPhone = getStringValue(event, "customerPhone");
        BigDecimal refundAmount = getBigDecimal(event, "refundAmount");

        notificationService.sendRefundProcessed(
                paymentId, bookingId, userId,
                customerName, customerEmail, customerPhone, refundAmount);
    }

    // ==================== Helper Methods ====================

    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    private Long getLongValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).longValue();
        return Long.parseLong(value.toString());
    }

    private BigDecimal getBigDecimal(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return null;
        if (value instanceof Number) return BigDecimal.valueOf(((Number) value).doubleValue());
        return new BigDecimal(value.toString());
    }
}

