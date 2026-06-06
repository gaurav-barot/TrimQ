package com.trimq.user.service;

import com.trimq.common.exception.BadRequestException;
import com.trimq.common.util.PassCodeGenerator;
import com.trimq.user.config.OtpConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Service for OTP generation and verification.
 * Uses Redis for OTP storage with TTL.
 * Follows Single Responsibility Principle.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

    private final RedisTemplate<String, String> redisTemplate;
    private final OtpConfig otpConfig;
    private final SmsService smsService;

    private static final String OTP_KEY_PREFIX = "otp:";
    private static final String OTP_ATTEMPTS_PREFIX = "otp:attempts:";

    /**
     * Generate and send OTP to phone number.
     */
    public void sendOtp(String phone, String purpose) {
        // Check rate limiting
        String attemptsKey = OTP_ATTEMPTS_PREFIX + phone;
        String attemptsStr = redisTemplate.opsForValue().get(attemptsKey);
        int attempts = attemptsStr != null ? Integer.parseInt(attemptsStr) : 0;

        if (attempts >= otpConfig.getMaxAttempts()) {
            throw new BadRequestException("Too many OTP requests. Please try again later.");
        }

        // Generate OTP
        String otp = PassCodeGenerator.generateOtp(otpConfig.getLength());
        
        // Store OTP in Redis with expiry
        String otpKey = OTP_KEY_PREFIX + phone;
        redisTemplate.opsForValue().set(otpKey, otp, otpConfig.getExpiryMinutes(), TimeUnit.MINUTES);
        
        // Increment attempts
        redisTemplate.opsForValue().set(attemptsKey, String.valueOf(attempts + 1), 
                otpConfig.getExpiryMinutes(), TimeUnit.MINUTES);

        // Send OTP via SMS
        smsService.sendOtp(phone, otp, purpose);
        
        log.info("OTP sent to phone: {}****{}", phone.substring(0, 3), phone.substring(7));
    }

    /**
     * Verify OTP for phone number.
     */
    public boolean verifyOtp(String phone, String otp) {
        String otpKey = OTP_KEY_PREFIX + phone;
        String storedOtp = redisTemplate.opsForValue().get(otpKey);

        if (storedOtp == null) {
            log.warn("No OTP found for phone: {}", phone);
            return false;
        }

        if (storedOtp.equals(otp)) {
            // Delete OTP after successful verification
            redisTemplate.delete(otpKey);
            redisTemplate.delete(OTP_ATTEMPTS_PREFIX + phone);
            log.info("OTP verified successfully for phone: {}****{}", 
                    phone.substring(0, 3), phone.substring(7));
            return true;
        }

        log.warn("Invalid OTP for phone: {}", phone);
        return false;
    }

    /**
     * Invalidate OTP for phone number.
     */
    public void invalidateOtp(String phone) {
        redisTemplate.delete(OTP_KEY_PREFIX + phone);
    }
}

