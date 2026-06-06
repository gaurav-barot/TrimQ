package com.trimq.common.util;

import com.trimq.common.constants.AppConstants;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility class to generate unique booking pass codes.
 * Format: TQ-YYYYMMDD-XXXX (e.g., TQ-20260115-7829)
 */
public final class PassCodeGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private PassCodeGenerator() {
        // Prevent instantiation
    }

    /**
     * Generate a unique pass code with date prefix.
     * Format: TQ-YYYYMMDD-XXXX
     */
    public static String generatePassCode() {
        String datePart = LocalDate.now().format(DATE_FORMATTER);
        String randomPart = generateRandomString(4);
        return String.format("%s-%s-%s", AppConstants.PASS_PREFIX, datePart, randomPart);
    }

    /**
     * Generate a random alphanumeric string.
     */
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }
        return sb.toString();
    }

    /**
     * Generate a numeric OTP.
     */
    public static String generateOtp(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * Validate pass code format.
     */
    public static boolean isValidPassCode(String passCode) {
        if (passCode == null || passCode.isBlank()) {
            return false;
        }
        // Format: TQ-YYYYMMDD-XXXX
        return passCode.matches("^TQ-\\d{8}-[A-Z0-9]{4}$");
    }
}

