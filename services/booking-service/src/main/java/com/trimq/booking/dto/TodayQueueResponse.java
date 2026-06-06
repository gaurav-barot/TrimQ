package com.trimq.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Response DTO for shop's today queue (shop owner view).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodayQueueResponse {
    
    private Long shopId;
    private LocalDate date;
    private int totalBookings;
    private int completedBookings;
    private int upcomingBookings;
    private int cancelledBookings;
    private BookingSummaryResponse currentBooking;
    private BookingSummaryResponse nextBooking;
    private List<BookingSummaryResponse> queue;
}

