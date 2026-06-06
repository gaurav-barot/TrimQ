package com.trimq.notification.service;

import com.trimq.common.enums.NotificationType;
import com.trimq.notification.entity.NotificationLog;
import com.trimq.notification.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Main notification service - orchestrates email and SMS sending.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final EmailService emailService;
    private final SmsService smsService;
    private final NotificationLogRepository logRepository;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm a");

    /**
     * Send booking confirmation notification.
     */
    @Transactional
    public void sendBookingConfirmation(Long bookingId, Long userId, String customerName,
                                         String customerEmail, String customerPhone,
                                         String shopName, String serviceName, String staffName,
                                         LocalDate bookingDate, LocalTime startTime,
                                         BigDecimal amount, String passCode, Integer tokenNumber) {
        
        log.info("Sending booking confirmation: bookingId={}", bookingId);

        // Check if already sent
        if (logRepository.existsByBookingIdAndTypeAndChannel(bookingId, 
                NotificationType.BOOKING_CONFIRMATION, NotificationLog.Channel.EMAIL)) {
            log.info("Booking confirmation already sent for bookingId={}", bookingId);
            return;
        }

        // Prepare template variables
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("shopName", shopName);
        variables.put("serviceName", serviceName);
        variables.put("staffName", staffName);
        variables.put("bookingDate", bookingDate.format(DATE_FORMAT));
        variables.put("startTime", startTime.format(TIME_FORMAT));
        variables.put("amount", amount);
        variables.put("passCode", passCode);
        variables.put("tokenNumber", tokenNumber);

        // Send email
        if (customerEmail != null && !customerEmail.isEmpty()) {
            boolean emailSent = emailService.sendTemplateEmail(
                    customerEmail,
                    "Booking Confirmed - " + shopName,
                    "booking-confirmation",
                    variables);

            logNotification(bookingId, null, userId, NotificationType.BOOKING_CONFIRMATION,
                    NotificationLog.Channel.EMAIL, customerEmail, 
                    "Booking Confirmed - " + shopName,
                    emailSent ? NotificationLog.Status.SENT : NotificationLog.Status.FAILED,
                    emailSent ? null : "Email sending failed");
        }

        // Send SMS
        if (customerPhone != null && !customerPhone.isEmpty()) {
            String smsMessage = buildBookingConfirmationSms(shopName, serviceName, 
                    bookingDate, startTime, passCode, tokenNumber);
            
            String sid = smsService.sendSms(customerPhone, smsMessage);

            logNotification(bookingId, null, userId, NotificationType.BOOKING_CONFIRMATION,
                    NotificationLog.Channel.SMS, customerPhone, null,
                    sid != null ? NotificationLog.Status.SENT : NotificationLog.Status.FAILED,
                    sid != null ? null : "SMS sending failed");
        }
    }

    /**
     * Send booking reminder notification.
     */
    @Transactional
    public void sendBookingReminder(Long bookingId, Long userId, String customerName,
                                     String customerEmail, String customerPhone,
                                     String shopName, String serviceName,
                                     LocalDate bookingDate, LocalTime startTime,
                                     String passCode, Integer tokenNumber) {
        
        log.info("Sending booking reminder: bookingId={}", bookingId);

        // Check if already sent
        if (logRepository.existsByBookingIdAndTypeAndChannel(bookingId, 
                NotificationType.BOOKING_REMINDER, NotificationLog.Channel.SMS)) {
            log.info("Booking reminder already sent for bookingId={}", bookingId);
            return;
        }

        // Send SMS reminder
        if (customerPhone != null && !customerPhone.isEmpty()) {
            String smsMessage = String.format(
                    "Reminder: Your appointment at %s is in 1 hour (%s). Pass: %s, Token: #%d. - TrimQ",
                    shopName, startTime.format(TIME_FORMAT), passCode, tokenNumber);
            
            String sid = smsService.sendSms(customerPhone, smsMessage);

            logNotification(bookingId, null, userId, NotificationType.BOOKING_REMINDER,
                    NotificationLog.Channel.SMS, customerPhone, null,
                    sid != null ? NotificationLog.Status.SENT : NotificationLog.Status.FAILED,
                    sid != null ? null : "SMS sending failed");
        }
    }

    /**
     * Send booking cancellation notification.
     */
    @Transactional
    public void sendBookingCancellation(Long bookingId, Long userId, String customerName,
                                         String customerEmail, String customerPhone,
                                         String shopName, String serviceName,
                                         LocalDate bookingDate, LocalTime startTime,
                                         String cancellationReason) {
        
        log.info("Sending booking cancellation: bookingId={}", bookingId);

        // Prepare template variables
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("shopName", shopName);
        variables.put("serviceName", serviceName);
        variables.put("bookingDate", bookingDate.format(DATE_FORMAT));
        variables.put("startTime", startTime.format(TIME_FORMAT));
        variables.put("cancellationReason", cancellationReason);

        // Send email
        if (customerEmail != null && !customerEmail.isEmpty()) {
            boolean emailSent = emailService.sendTemplateEmail(
                    customerEmail,
                    "Booking Cancelled - " + shopName,
                    "booking-cancellation",
                    variables);

            logNotification(bookingId, null, userId, NotificationType.BOOKING_CANCELLATION,
                    NotificationLog.Channel.EMAIL, customerEmail, 
                    "Booking Cancelled",
                    emailSent ? NotificationLog.Status.SENT : NotificationLog.Status.FAILED,
                    emailSent ? null : "Email sending failed");
        }

        // Send SMS
        if (customerPhone != null && !customerPhone.isEmpty()) {
            String smsMessage = String.format(
                    "Your booking at %s on %s has been cancelled. Reason: %s. - TrimQ",
                    shopName, bookingDate.format(DATE_FORMAT), cancellationReason);
            
            String sid = smsService.sendSms(customerPhone, smsMessage);

            logNotification(bookingId, null, userId, NotificationType.BOOKING_CANCELLATION,
                    NotificationLog.Channel.SMS, customerPhone, null,
                    sid != null ? NotificationLog.Status.SENT : NotificationLog.Status.FAILED,
                    sid != null ? null : "SMS sending failed");
        }
    }

    /**
     * Send payment success notification.
     */
    @Transactional
    public void sendPaymentSuccess(Long paymentId, Long bookingId, Long userId,
                                    String customerName, String customerEmail, String customerPhone,
                                    BigDecimal amount, String razorpayPaymentId) {
        
        log.info("Sending payment success: paymentId={}", paymentId);

        // Prepare template variables
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("amount", amount);
        variables.put("transactionId", razorpayPaymentId);
        variables.put("date", LocalDate.now().format(DATE_FORMAT));

        // Send email
        if (customerEmail != null && !customerEmail.isEmpty()) {
            boolean emailSent = emailService.sendTemplateEmail(
                    customerEmail,
                    "Payment Received - ₹" + amount,
                    "payment-success",
                    variables);

            logNotification(bookingId, paymentId, userId, NotificationType.PAYMENT_SUCCESS,
                    NotificationLog.Channel.EMAIL, customerEmail, 
                    "Payment Received",
                    emailSent ? NotificationLog.Status.SENT : NotificationLog.Status.FAILED,
                    emailSent ? null : "Email sending failed");
        }

        // Send SMS
        if (customerPhone != null && !customerPhone.isEmpty()) {
            String smsMessage = String.format(
                    "Payment of ₹%.2f received. Txn ID: %s. Your booking is confirmed! - TrimQ",
                    amount, razorpayPaymentId);
            
            String sid = smsService.sendSms(customerPhone, smsMessage);

            logNotification(bookingId, paymentId, userId, NotificationType.PAYMENT_SUCCESS,
                    NotificationLog.Channel.SMS, customerPhone, null,
                    sid != null ? NotificationLog.Status.SENT : NotificationLog.Status.FAILED,
                    sid != null ? null : "SMS sending failed");
        }
    }

    /**
     * Send payment failed notification.
     */
    @Transactional
    public void sendPaymentFailed(Long paymentId, Long bookingId, Long userId,
                                   String customerEmail, String customerPhone,
                                   String failureReason) {
        
        log.info("Sending payment failed: paymentId={}", paymentId);

        // Send SMS
        if (customerPhone != null && !customerPhone.isEmpty()) {
            String smsMessage = String.format(
                    "Payment failed. Reason: %s. Please try again. - TrimQ",
                    failureReason != null ? failureReason : "Unknown error");
            
            String sid = smsService.sendSms(customerPhone, smsMessage);

            logNotification(bookingId, paymentId, userId, NotificationType.PAYMENT_FAILED,
                    NotificationLog.Channel.SMS, customerPhone, null,
                    sid != null ? NotificationLog.Status.SENT : NotificationLog.Status.FAILED,
                    sid != null ? null : "SMS sending failed");
        }
    }

    /**
     * Send refund processed notification.
     */
    @Transactional
    public void sendRefundProcessed(Long paymentId, Long bookingId, Long userId,
                                     String customerName, String customerEmail, String customerPhone,
                                     BigDecimal refundAmount) {
        
        log.info("Sending refund processed: paymentId={}", paymentId);

        // Prepare template variables
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("refundAmount", refundAmount);
        variables.put("date", LocalDate.now().format(DATE_FORMAT));

        // Send email
        if (customerEmail != null && !customerEmail.isEmpty()) {
            boolean emailSent = emailService.sendTemplateEmail(
                    customerEmail,
                    "Refund Processed - ₹" + refundAmount,
                    "refund-processed",
                    variables);

            logNotification(bookingId, paymentId, userId, NotificationType.REFUND_PROCESSED,
                    NotificationLog.Channel.EMAIL, customerEmail, 
                    "Refund Processed",
                    emailSent ? NotificationLog.Status.SENT : NotificationLog.Status.FAILED,
                    emailSent ? null : "Email sending failed");
        }

        // Send SMS
        if (customerPhone != null && !customerPhone.isEmpty()) {
            String smsMessage = String.format(
                    "Refund of ₹%.2f has been processed. It will reflect in 5-7 business days. - TrimQ",
                    refundAmount);
            
            String sid = smsService.sendSms(customerPhone, smsMessage);

            logNotification(bookingId, paymentId, userId, NotificationType.REFUND_PROCESSED,
                    NotificationLog.Channel.SMS, customerPhone, null,
                    sid != null ? NotificationLog.Status.SENT : NotificationLog.Status.FAILED,
                    sid != null ? null : "SMS sending failed");
        }
    }

    // ==================== Helper Methods ====================

    private String buildBookingConfirmationSms(String shopName, String serviceName,
                                                LocalDate bookingDate, LocalTime startTime,
                                                String passCode, Integer tokenNumber) {
        return String.format(
                "Booking confirmed at %s for %s on %s at %s. Pass: %s, Token: #%d. Show this at the shop. - TrimQ",
                shopName, serviceName,
                bookingDate.format(DATE_FORMAT),
                startTime.format(TIME_FORMAT),
                passCode, tokenNumber);
    }

    private void logNotification(Long bookingId, Long paymentId, Long userId,
                                  NotificationType type, NotificationLog.Channel channel,
                                  String recipient, String subject,
                                  NotificationLog.Status status, String errorMessage) {
        NotificationLog log = NotificationLog.builder()
                .bookingId(bookingId)
                .paymentId(paymentId)
                .userId(userId)
                .type(type)
                .channel(channel)
                .recipient(recipient)
                .subject(subject)
                .status(status)
                .errorMessage(errorMessage)
                .build();
        
        logRepository.save(log);
    }
}

