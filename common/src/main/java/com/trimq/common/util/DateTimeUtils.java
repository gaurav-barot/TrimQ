package com.trimq.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for date and time operations.
 */
public final class DateTimeUtils {

    private static final ZoneId INDIA_ZONE = ZoneId.of("Asia/Kolkata");
    
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DISPLAY_DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");
    public static final DateTimeFormatter DISPLAY_TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm a");

    private DateTimeUtils() {
        // Prevent instantiation
    }

    /**
     * Get current date in India timezone.
     */
    public static LocalDate todayIndia() {
        return LocalDate.now(INDIA_ZONE);
    }

    /**
     * Get current time in India timezone.
     */
    public static LocalTime nowIndia() {
        return LocalTime.now(INDIA_ZONE);
    }

    /**
     * Get current datetime in India timezone.
     */
    public static LocalDateTime nowDateTimeIndia() {
        return LocalDateTime.now(INDIA_ZONE);
    }

    /**
     * Generate time slots between start and end time.
     */
    public static List<LocalTime> generateTimeSlots(LocalTime start, LocalTime end, int slotDurationMinutes) {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime current = start;
        
        while (current.plusMinutes(slotDurationMinutes).compareTo(end) <= 0) {
            slots.add(current);
            current = current.plusMinutes(slotDurationMinutes);
        }
        
        return slots;
    }

    /**
     * Check if a time is within a range.
     */
    public static boolean isTimeInRange(LocalTime time, LocalTime start, LocalTime end) {
        return !time.isBefore(start) && !time.isAfter(end);
    }

    /**
     * Get hours until a specific datetime.
     */
    public static long hoursUntil(LocalDateTime dateTime) {
        return ChronoUnit.HOURS.between(nowDateTimeIndia(), dateTime);
    }

    /**
     * Check if booking can be cancelled for free (before cutoff hours).
     */
    public static boolean canCancelForFree(LocalDate bookingDate, LocalTime bookingTime, int freeHours) {
        LocalDateTime bookingDateTime = LocalDateTime.of(bookingDate, bookingTime);
        return hoursUntil(bookingDateTime) >= freeHours;
    }

    /**
     * Format time for display (12-hour format).
     */
    public static String formatTimeForDisplay(LocalTime time) {
        return time.format(DISPLAY_TIME_FORMAT);
    }

    /**
     * Format date for display.
     */
    public static String formatDateForDisplay(LocalDate date) {
        return date.format(DISPLAY_DATE_FORMAT);
    }
}

