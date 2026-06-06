package com.trimq.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for sending SMS messages.
 * Currently uses mock implementation.
 * Can be extended to integrate with Twilio, MSG91, etc.
 * 
 * Follows Open/Closed Principle - extend for different providers.
 */
@Slf4j
@Service
public class SmsService {

    @Value("${sms.provider:mock}")
    private String provider;

    @Value("${sms.api-key:}")
    private String apiKey;

    /**
     * Send OTP via SMS.
     */
    public void sendOtp(String phone, String otp, String purpose) {
        String message = buildOtpMessage(otp, purpose);
        sendSms(phone, message);
    }

    /**
     * Send SMS message.
     */
    public void sendSms(String phone, String message) {
        if ("mock".equals(provider)) {
            // Mock implementation - just log
            log.info("📱 [MOCK SMS] To: +91{}, Message: {}", phone, message);
            return;
        }

        // TODO: Implement actual SMS sending based on provider
        switch (provider.toLowerCase()) {
            case "twilio":
                sendViaTwilio(phone, message);
                break;
            case "msg91":
                sendViaMsg91(phone, message);
                break;
            default:
                log.warn("Unknown SMS provider: {}, using mock", provider);
                log.info("📱 [MOCK SMS] To: +91{}, Message: {}", phone, message);
        }
    }

    /**
     * Build OTP message.
     */
    private String buildOtpMessage(String otp, String purpose) {
        return switch (purpose) {
            case "REGISTRATION" -> String.format("Welcome to TrimQ! Your OTP is %s. Valid for 5 minutes.", otp);
            case "LOGIN" -> String.format("Your TrimQ login OTP is %s. Valid for 5 minutes.", otp);
            case "PASSWORD_RESET" -> String.format("Your TrimQ password reset OTP is %s. Valid for 5 minutes.", otp);
            default -> String.format("Your TrimQ OTP is %s. Valid for 5 minutes.", otp);
        };
    }

    /**
     * Send via Twilio (placeholder).
     */
    private void sendViaTwilio(String phone, String message) {
        // TODO: Implement Twilio integration
        log.info("Sending SMS via Twilio to: {}", phone);
    }

    /**
     * Send via MSG91 (placeholder).
     */
    private void sendViaMsg91(String phone, String message) {
        // TODO: Implement MSG91 integration
        log.info("Sending SMS via MSG91 to: {}", phone);
    }
}

