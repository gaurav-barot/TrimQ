package com.trimq.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

/**
 * Response DTO for working hours.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkingHoursResponse {

    private String id;
    private DayOfWeek dayOfWeek;
    private String dayName;
    private Boolean isOpen;
    private String openTime;
    private String closeTime;
    private String breakStart;
    private String breakEnd;
}

