package com.trimq.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for slot lock operation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlotLockResponse {
    
    private boolean locked;
    private String lockId;
    private LocalDateTime expiresAt;
    private String message;
}

