package com.trimq.booking.service;

import com.trimq.booking.entity.Booking;
import com.trimq.common.constants.KafkaConstants;
import com.trimq.common.event.BookingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Service for publishing booking events to Kafka.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookingEventPublisher {

    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    /**
     * Publish booking created event.
     */
    public void publishBookingCreated(Booking booking) {
        BookingEvent event = buildEvent(booking, KafkaConstants.EVENT_BOOKING_CREATED);
        publish(event);
    }

    /**
     * Publish booking confirmed event (after payment).
     */
    public void publishBookingConfirmed(Booking booking) {
        BookingEvent event = buildEvent(booking, KafkaConstants.EVENT_BOOKING_CONFIRMED);
        publish(event);
    }

    /**
     * Publish booking cancelled event.
     */
    public void publishBookingCancelled(Booking booking) {
        BookingEvent event = buildEvent(booking, KafkaConstants.EVENT_BOOKING_CANCELLED);
        event.setCancellationReason(booking.getCancellationReason());
        publish(event);
    }

    /**
     * Publish booking completed event.
     */
    public void publishBookingCompleted(Booking booking) {
        BookingEvent event = buildEvent(booking, KafkaConstants.EVENT_BOOKING_COMPLETED);
        publish(event);
    }

    /**
     * Publish booking reminder event (1 hour before).
     */
    public void publishBookingReminder(Booking booking) {
        BookingEvent event = buildEvent(booking, KafkaConstants.EVENT_BOOKING_REMINDER);
        publish(event);
    }

    private BookingEvent buildEvent(Booking booking, String eventType) {
        return BookingEvent.builder()
                .bookingId(booking.getId())
                .userId(booking.getUserId())
                .shopId(booking.getShopId())
                .serviceId(booking.getServiceId())
                .shopName(booking.getShopName())
                .serviceName(booking.getServiceName())
                .staffName(booking.getStaffName())
                .bookingDate(booking.getBookingDate())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .amount(booking.getAmount())
                .passCode(booking.getPassCode())
                .tokenNumber(booking.getTokenNumber())
                .customerName(booking.getCustomerName())
                .customerPhone(booking.getCustomerPhone())
                .customerEmail(booking.getCustomerEmail())
                .eventType(eventType)
                .build();
    }

    private void publish(BookingEvent event) {
        log.info("Publishing booking event: type={}, bookingId={}", 
                event.getEventType(), event.getBookingId());

        CompletableFuture<SendResult<String, BookingEvent>> future = 
                kafkaTemplate.send(KafkaConstants.TOPIC_BOOKING_EVENTS, 
                        String.valueOf(event.getBookingId()), event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish booking event: bookingId={}", 
                        event.getBookingId(), ex);
            } else {
                log.debug("Booking event published: bookingId={}, offset={}", 
                        event.getBookingId(), 
                        result.getRecordMetadata().offset());
            }
        });
    }
}

