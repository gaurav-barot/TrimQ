package com.trimq.notification.consumer;

import com.trimq.common.constants.KafkaConstants;
import com.trimq.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

/**
 * Kafka consumer for booking events.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BookingEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = KafkaConstants.TOPIC_BOOKING_EVENTS, groupId = "notification-service")
    public void handleBookingEvent(Map<String, Object> event) {
        String eventType = getStringValue(event, "eventType");
        log.info("Received booking event: type={}", eventType);

        try {
            switch (eventType) {
                case KafkaConstants.EVENT_BOOKING_CREATED:
                case KafkaConstants.EVENT_BOOKING_CONFIRMED:
                    handleBookingConfirmed(event);
                    break;
                case KafkaConstants.EVENT_BOOKING_CANCELLED:
                    handleBookingCancelled(event);
                    break;
                case KafkaConstants.EVENT_BOOKING_REMINDER:
                    handleBookingReminder(event);
                    break;
                case KafkaConstants.EVENT_BOOKING_COMPLETED:
                    handleBookingCompleted(event);
                    break;
                default:
                    log.debug("Unhandled booking event type: {}", eventType);
            }
        } catch (Exception e) {
            log.error("Error processing booking event: {}", eventType, e);
        }
    }

    private void handleBookingConfirmed(Map<String, Object> event) {
        Long bookingId = getLongValue(event, "bookingId");
        Long userId = getLongValue(event, "userId");
        String customerName = getStringValue(event, "customerName");
        String customerEmail = getStringValue(event, "customerEmail");
        String customerPhone = getStringValue(event, "customerPhone");
        String shopName = getStringValue(event, "shopName");
        String serviceName = getStringValue(event, "serviceName");
        String staffName = getStringValue(event, "staffName");
        LocalDate bookingDate = getLocalDate(event, "bookingDate");
        LocalTime startTime = getLocalTime(event, "startTime");
        BigDecimal amount = getBigDecimal(event, "amount");
        String passCode = getStringValue(event, "passCode");
        Integer tokenNumber = getIntValue(event, "tokenNumber");

        notificationService.sendBookingConfirmation(
                bookingId, userId, customerName, customerEmail, customerPhone,
                shopName, serviceName, staffName, bookingDate, startTime,
                amount, passCode, tokenNumber);
    }

    private void handleBookingCancelled(Map<String, Object> event) {
        Long bookingId = getLongValue(event, "bookingId");
        Long userId = getLongValue(event, "userId");
        String customerName = getStringValue(event, "customerName");
        String customerEmail = getStringValue(event, "customerEmail");
        String customerPhone = getStringValue(event, "customerPhone");
        String shopName = getStringValue(event, "shopName");
        String serviceName = getStringValue(event, "serviceName");
        LocalDate bookingDate = getLocalDate(event, "bookingDate");
        LocalTime startTime = getLocalTime(event, "startTime");
        String cancellationReason = getStringValue(event, "cancellationReason");

        notificationService.sendBookingCancellation(
                bookingId, userId, customerName, customerEmail, customerPhone,
                shopName, serviceName, bookingDate, startTime, cancellationReason);
    }

    private void handleBookingReminder(Map<String, Object> event) {
        Long bookingId = getLongValue(event, "bookingId");
        Long userId = getLongValue(event, "userId");
        String customerName = getStringValue(event, "customerName");
        String customerEmail = getStringValue(event, "customerEmail");
        String customerPhone = getStringValue(event, "customerPhone");
        String shopName = getStringValue(event, "shopName");
        String serviceName = getStringValue(event, "serviceName");
        LocalDate bookingDate = getLocalDate(event, "bookingDate");
        LocalTime startTime = getLocalTime(event, "startTime");
        String passCode = getStringValue(event, "passCode");
        Integer tokenNumber = getIntValue(event, "tokenNumber");

        notificationService.sendBookingReminder(
                bookingId, userId, customerName, customerEmail, customerPhone,
                shopName, serviceName, bookingDate, startTime, passCode, tokenNumber);
    }

    private void handleBookingCompleted(Map<String, Object> event) {
        // Could send a "Thank you" or review request
        log.info("Booking completed: {}", getLongValue(event, "bookingId"));
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

    private Integer getIntValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        return Integer.parseInt(value.toString());
    }

    private BigDecimal getBigDecimal(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return null;
        if (value instanceof Number) return BigDecimal.valueOf(((Number) value).doubleValue());
        return new BigDecimal(value.toString());
    }

    private LocalDate getLocalDate(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return null;
        if (value instanceof String) return LocalDate.parse((String) value);
        // Handle array format [2024, 12, 31]
        if (value instanceof java.util.List) {
            @SuppressWarnings("unchecked")
            java.util.List<Integer> list = (java.util.List<Integer>) value;
            return LocalDate.of(list.get(0), list.get(1), list.get(2));
        }
        return null;
    }

    private LocalTime getLocalTime(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return null;
        if (value instanceof String) return LocalTime.parse((String) value);
        // Handle array format [10, 30]
        if (value instanceof java.util.List) {
            @SuppressWarnings("unchecked")
            java.util.List<Integer> list = (java.util.List<Integer>) value;
            return LocalTime.of(list.get(0), list.get(1));
        }
        return null;
    }
}

