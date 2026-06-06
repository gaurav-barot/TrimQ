package com.trimq.booking.scheduler;

import com.trimq.booking.entity.Booking;
import com.trimq.booking.repository.BookingRepository;
import com.trimq.booking.service.BookingEventPublisher;
import com.trimq.common.enums.BookingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Scheduler for sending booking reminders.
 * 
 * Sends reminder notifications 1 hour before the booking.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BookingReminderScheduler {

    private final BookingRepository bookingRepository;
    private final BookingEventPublisher eventPublisher;

    /**
     * Send reminders for bookings starting in the next hour.
     * Runs every 15 minutes.
     */
    @Scheduled(cron = "0 */15 * * * *") // Every 15 minutes
    public void sendBookingReminders() {
        log.debug("Running booking reminder scheduler...");

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        LocalTime oneHourLater = now.plusHours(1);

        // Find confirmed bookings in the next hour
        List<Booking> upcomingBookings = bookingRepository.findByShopIdAndDateAndStatuses(
                null, // Will be filtered in query
                today,
                List.of(BookingStatus.CONFIRMED)
        ).stream()
                .filter(b -> b.getStartTime().isAfter(now) && b.getStartTime().isBefore(oneHourLater))
                .toList();

        log.info("Found {} bookings requiring reminders", upcomingBookings.size());

        for (Booking booking : upcomingBookings) {
            try {
                eventPublisher.publishBookingReminder(booking);
                log.debug("Reminder sent for booking: {}", booking.getId());
            } catch (Exception e) {
                log.error("Failed to send reminder for booking: {}", booking.getId(), e);
            }
        }
    }

    /**
     * Mark no-show bookings.
     * Runs every hour.
     */
    @Scheduled(cron = "0 0 * * * *") // Every hour
    public void markNoShowBookings() {
        log.debug("Running no-show checker...");

        LocalDate today = LocalDate.now();
        LocalTime thirtyMinutesAgo = LocalTime.now().minusMinutes(30);

        // Find confirmed bookings that started 30+ minutes ago
        List<Booking> potentialNoShows = bookingRepository.findByShopIdAndDateAndStatuses(
                null,
                today,
                List.of(BookingStatus.CONFIRMED)
        ).stream()
                .filter(b -> b.getStartTime().isBefore(thirtyMinutesAgo))
                .toList();

        for (Booking booking : potentialNoShows) {
            // Only mark as no-show if still confirmed after 30 mins past start time
            booking.setStatus(BookingStatus.NO_SHOW);
            bookingRepository.save(booking);
            log.info("Marked booking {} as NO_SHOW", booking.getId());
        }
    }
}

