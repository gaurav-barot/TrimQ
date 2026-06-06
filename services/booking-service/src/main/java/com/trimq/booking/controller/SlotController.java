package com.trimq.booking.controller;

import com.trimq.booking.dto.DaySlotResponse;
import com.trimq.booking.dto.SlotLockRequest;
import com.trimq.booking.dto.SlotLockResponse;
import com.trimq.booking.service.SlotService;
import com.trimq.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for slot management.
 */
@RestController
@RequestMapping("/slots")
@RequiredArgsConstructor
@Tag(name = "Slots", description = "Time slot management APIs")
public class SlotController {

    private final SlotService slotService;

    /**
     * Get available slots for a shop on a specific date.
     */
    @GetMapping
    @Operation(summary = "Get available slots for a date")
    public ResponseEntity<ApiResponse<DaySlotResponse>> getSlots(
            @RequestParam Long shopId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false, defaultValue = "30") Integer duration) {
        
        DaySlotResponse slots = slotService.getAvailableSlots(shopId, date, duration);
        return ResponseEntity.ok(ApiResponse.success("Slots retrieved successfully", slots));
    }

    /**
     * Get slots for multiple days (calendar view).
     */
    @GetMapping("/calendar")
    @Operation(summary = "Get slots for multiple days")
    public ResponseEntity<ApiResponse<List<DaySlotResponse>>> getSlotsForDays(
            @RequestParam Long shopId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false, defaultValue = "7") int days,
            @RequestParam(required = false, defaultValue = "30") Integer duration) {
        
        if (startDate == null) {
            startDate = LocalDate.now();
        }
        
        List<DaySlotResponse> slots = slotService.getAvailableSlotsForDays(shopId, startDate, days, duration);
        return ResponseEntity.ok(ApiResponse.success("Slots retrieved successfully", slots));
    }

    /**
     * Lock a slot temporarily (before payment).
     */
    @PostMapping("/lock")
    @Operation(summary = "Lock a slot temporarily")
    public ResponseEntity<ApiResponse<SlotLockResponse>> lockSlot(
            @Valid @RequestBody SlotLockRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        
        SlotLockResponse response = slotService.lockSlot(request, userId);
        
        if (response.isLocked()) {
            return ResponseEntity.ok(ApiResponse.success(response.getMessage(), response));
        } else {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(response.getMessage()));
        }
    }

    /**
     * Release a slot lock.
     */
    @DeleteMapping("/lock")
    @Operation(summary = "Release a slot lock")
    public ResponseEntity<ApiResponse<Void>> releaseSlotLock(
            @RequestParam Long shopId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) java.time.LocalTime startTime,
            @RequestParam String lockId) {
        
        boolean released = slotService.releaseSlotLock(shopId, date, startTime, lockId);
        
        if (released) {
            return ResponseEntity.ok(ApiResponse.success("Slot lock released", null));
        } else {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to release slot lock"));
        }
    }
}

