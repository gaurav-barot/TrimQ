package com.trimq.payment.service;

import com.trimq.common.constants.KafkaConstants;
import com.trimq.common.event.PaymentEvent;
import com.trimq.payment.entity.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Service for publishing payment events to Kafka.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventPublisher {

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    /**
     * Publish payment success event.
     */
    public void publishPaymentSuccess(Payment payment) {
        PaymentEvent event = buildEvent(payment, KafkaConstants.EVENT_PAYMENT_SUCCESS);
        publish(event);
    }

    /**
     * Publish payment failed event.
     */
    public void publishPaymentFailed(Payment payment) {
        PaymentEvent event = buildEvent(payment, KafkaConstants.EVENT_PAYMENT_FAILED);
        event.setFailureReason(payment.getFailureReason());
        publish(event);
    }

    /**
     * Publish refund processed event.
     */
    public void publishRefundProcessed(Payment payment) {
        PaymentEvent event = buildEvent(payment, KafkaConstants.EVENT_REFUND_PROCESSED);
        event.setRefundId(payment.getRefundId());
        event.setRefundAmount(payment.getRefundAmount());
        publish(event);
    }

    private PaymentEvent buildEvent(Payment payment, String eventType) {
        return PaymentEvent.builder()
                .paymentId(payment.getId())
                .bookingId(payment.getBookingId())
                .userId(payment.getUserId())
                .shopId(payment.getShopId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .razorpayOrderId(payment.getRazorpayOrderId())
                .razorpayPaymentId(payment.getRazorpayPaymentId())
                .status(payment.getStatus())
                .paymentMethod(payment.getPaymentMethod())
                .customerName(payment.getCustomerName())
                .customerPhone(payment.getCustomerPhone())
                .customerEmail(payment.getCustomerEmail())
                .eventType(eventType)
                .build();
    }

    private void publish(PaymentEvent event) {
        log.info("Publishing payment event: type={}, paymentId={}", 
                event.getEventType(), event.getPaymentId());

        CompletableFuture<SendResult<String, PaymentEvent>> future = 
                kafkaTemplate.send(KafkaConstants.TOPIC_PAYMENT_EVENTS, 
                        String.valueOf(event.getPaymentId()), event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish payment event: paymentId={}", 
                        event.getPaymentId(), ex);
            } else {
                log.debug("Payment event published: paymentId={}, offset={}", 
                        event.getPaymentId(), 
                        result.getRecordMetadata().offset());
            }
        });
    }
}

