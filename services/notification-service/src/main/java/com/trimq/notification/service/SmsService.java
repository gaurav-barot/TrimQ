package com.trimq.notification.service;

import com.trimq.notification.config.TwilioConfig;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Service for sending SMS using Twilio.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService {

    private final TwilioConfig twilioConfig;

    /**
     * Send an SMS.
     * 
     * @param to Phone number (with country code, e.g., +919876543210)
     * @param message SMS content
     * @return Message SID if sent, null otherwise
     */
    public String sendSms(String to, String message) {
        if (!twilioConfig.isEnabled()) {
            log.info("SMS disabled. Would send to: {}, message: {}", to, truncateForLog(message));
            return "mock-sid-" + System.currentTimeMillis();
        }

        try {
            // Ensure phone number has country code
            String formattedNumber = formatPhoneNumber(to);
            
            Message twilioMessage = Message.creator(
                    new PhoneNumber(formattedNumber),
                    new PhoneNumber(twilioConfig.getFromNumber()),
                    message
            ).create();
            
            String sid = twilioMessage.getSid();
            log.info("SMS sent to: {}, SID: {}", formattedNumber, sid);
            return sid;
            
        } catch (Exception e) {
            log.error("Failed to send SMS to: {}", to, e);
            return null;
        }
    }

    /**
     * Send SMS asynchronously.
     */
    @Async("notificationExecutor")
    public CompletableFuture<String> sendSmsAsync(String to, String message) {
        String result = sendSms(to, message);
        return CompletableFuture.completedFuture(result);
    }

    /**
     * Format phone number with India country code if not present.
     */
    private String formatPhoneNumber(String phone) {
        if (phone == null) return null;
        
        phone = phone.replaceAll("[^0-9+]", "");
        
        if (phone.startsWith("+")) {
            return phone;
        }
        
        if (phone.startsWith("91") && phone.length() == 12) {
            return "+" + phone;
        }
        
        if (phone.length() == 10) {
            return "+91" + phone;
        }
        
        return "+91" + phone;
    }

    private String truncateForLog(String message) {
        if (message == null) return null;
        return message.length() > 50 ? message.substring(0, 50) + "..." : message;
    }
}

