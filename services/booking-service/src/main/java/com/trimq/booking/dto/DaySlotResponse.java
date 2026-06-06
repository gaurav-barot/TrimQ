package com.trimq.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Response DTO for all slots in a day.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DaySlotResponse {
    
    private Long shopId;
    private LocalDate date;
    private String dayOfWeek;
    private boolean shopOpen;
    private List<SlotResponse> slots;
    private int totalSlots;
    private int availableSlots;
}

