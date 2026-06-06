package com.trimq.booking.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Service for generating unique pass codes.
 * 
 * Format: TQ-YYYYMMDD-XXXX
 * Example: TQ-20241231-A7B9
 */
@Service
public class PassCodeGenerator {

    private static final String ALPHANUMERIC = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 4;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * Generate a unique pass code for a booking.
     * 
     * @param bookingDate Date of the booking
     * @return Unique pass code (TQ-YYYYMMDD-XXXX)
     */
    public String generate(LocalDate bookingDate) {
        String dateStr = bookingDate.format(DATE_FORMAT);
        String randomPart = generateRandomCode();
        return "TQ-" + dateStr + "-" + randomPart;
    }

    /**
     * Generate a unique pass code for today.
     * 
     * @return Unique pass code
     */
    public String generate() {
        return generate(LocalDate.now());
    }

    /**
     * Generate random alphanumeric code.
     * 
     * @return Random code of CODE_LENGTH characters
     */
    private String generateRandomCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = RANDOM.nextInt(ALPHANUMERIC.length());
            code.append(ALPHANUMERIC.charAt(index));
        }
        return code.toString();
    }

    /**
     * Validate pass code format.
     * 
     * @param passCode Pass code to validate
     * @return true if valid format
     */
    public boolean isValidFormat(String passCode) {
        if (passCode == null || passCode.length() != 16) {
            return false;
        }
        return passCode.matches("^TQ-\\d{8}-[A-Z0-9]{4}$");
    }

    /**
     * Extract date from pass code.
     * 
     * @param passCode Pass code
     * @return Booking date, or null if invalid
     */
    public LocalDate extractDate(String passCode) {
        if (!isValidFormat(passCode)) {
            return null;
        }
        try {
            String dateStr = passCode.substring(3, 11);
            return LocalDate.parse(dateStr, DATE_FORMAT);
        } catch (Exception e) {
            return null;
        }
    }
}

