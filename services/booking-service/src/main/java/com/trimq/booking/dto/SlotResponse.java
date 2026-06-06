package com.trimq.booking.dto;

import com.trimq.common.enums.SlotStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * Response DTO for time slot availability.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlotResponse {
    
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer durationMinutes;
    private SlotStatus status;
    private Long staffId;
    private String staffName;
    private boolean available;
}

