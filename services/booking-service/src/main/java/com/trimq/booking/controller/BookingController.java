package com.trimq.booking.controller;

import com.trimq.booking.dto.*;
import com.trimq.booking.service.BookingService;
import com.trimq.common.dto.ApiResponse;
import com.trimq.common.dto.PagedResponse;
import com.trimq.common.enums.BookingStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.trimq.common.constants.AppConstants.*;

/**
 * REST controller for booking management.
 */
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "Booking management APIs")
public class BookingController {

    private final BookingService bookingService;

    /**
     * Create a new booking.
     */
    @PostMapping
    @Operation(summary = "Create a new booking")
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @Valid @RequestBody CreateBookingRequest request,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader(value = "X-User-Name", required = false) String userName,
            @RequestHeader(value = "X-User-Phone", required = false) String userPhone,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        
        BookingResponse booking = bookingService.createBooking(request, userId, 
                userName, userPhone, userEmail);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Booking created successfully", booking));
    }

    /**
     * Get booking by ID.
     */
    @GetMapping("/{bookingId}")
    @Operation(summary = "Get booking by ID")
    public ResponseEntity<ApiResponse<BookingResponse>> getBooking(
            @PathVariable Long bookingId,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles) {
        
        boolean isShopOwner = roles != null && roles.contains("SHOP_OWNER");
        BookingResponse booking = bookingService.getBooking(bookingId, userId, isShopOwner);
        
        return ResponseEntity.ok(ApiResponse.success("Booking retrieved", booking));
    }

    /**
     * Get user's bookings.
     */
    @GetMapping("/user")
    @Operation(summary = "Get current user's bookings")
    public ResponseEntity<ApiResponse<PagedResponse<BookingSummaryResponse>>> getUserBookings(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        
        PagedResponse<BookingSummaryResponse> bookings = 
                bookingService.getUserBookings(userId, pageNo, pageSize);
        
        return ResponseEntity.ok(ApiResponse.success("User bookings retrieved", bookings));
    }

    /**
     * Get user's upcoming bookings.
     */
    @GetMapping("/user/upcoming")
    @Operation(summary = "Get user's upcoming bookings")
    public ResponseEntity<ApiResponse<List<BookingSummaryResponse>>> getUserUpcomingBookings(
            @RequestHeader("X-User-Id") Long userId) {
        
        List<BookingSummaryResponse> bookings = bookingService.getUserUpcomingBookings(userId);
        return ResponseEntity.ok(ApiResponse.success("Upcoming bookings retrieved", bookings));
    }

    /**
     * Get shop's bookings.
     */
    @GetMapping("/shop/{shopId}")
    @Operation(summary = "Get shop's bookings (shop owner)")
    public ResponseEntity<ApiResponse<PagedResponse<BookingSummaryResponse>>> getShopBookings(
            @PathVariable Long shopId,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        
        PagedResponse<BookingSummaryResponse> bookings = 
                bookingService.getShopBookings(shopId, pageNo, pageSize);
        
        return ResponseEntity.ok(ApiResponse.success("Shop bookings retrieved", bookings));
    }

    /**
     * Get shop's today queue.
     */
    @GetMapping("/shop/{shopId}/today")
    @Operation(summary = "Get shop's today queue")
    public ResponseEntity<ApiResponse<TodayQueueResponse>> getShopTodayQueue(
            @PathVariable Long shopId) {
        
        TodayQueueResponse queue = bookingService.getShopTodayQueue(shopId);
        return ResponseEntity.ok(ApiResponse.success("Today's queue retrieved", queue));
    }

    /**
     * Cancel a booking.
     */
    @DeleteMapping("/{bookingId}")
    @Operation(summary = "Cancel a booking")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(
            @PathVariable Long bookingId,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @Valid @RequestBody CancelBookingRequest request) {
        
        boolean isShopOwner = roles != null && roles.contains("SHOP_OWNER");
        BookingResponse booking = bookingService.cancelBooking(bookingId, userId, 
                request.getReason(), isShopOwner);
        
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled", booking));
    }

    /**
     * Start a booking (shop owner action).
     */
    @PostMapping("/{bookingId}/start")
    @Operation(summary = "Start a booking (mark in progress)")
    public ResponseEntity<ApiResponse<BookingResponse>> startBooking(
            @PathVariable Long bookingId,
            @RequestHeader("X-Shop-Id") Long shopId) {
        
        BookingResponse booking = bookingService.startBooking(bookingId, shopId);
        return ResponseEntity.ok(ApiResponse.success("Booking started", booking));
    }

    /**
     * Complete a booking (shop owner action).
     */
    @PostMapping("/{bookingId}/complete")
    @Operation(summary = "Complete a booking")
    public ResponseEntity<ApiResponse<BookingResponse>> completeBooking(
            @PathVariable Long bookingId,
            @RequestHeader("X-Shop-Id") Long shopId) {
        
        BookingResponse booking = bookingService.completeBooking(bookingId, shopId);
        return ResponseEntity.ok(ApiResponse.success("Booking completed", booking));
    }

    /**
     * Validate a booking pass (QR code scan).
     */
    @PostMapping("/validate-pass")
    @Operation(summary = "Validate a booking pass")
    public ResponseEntity<ApiResponse<ValidatePassResponse>> validatePass(
            @Valid @RequestBody ValidatePassRequest request,
            @RequestHeader("X-Shop-Id") Long shopId) {
        
        ValidatePassResponse response = bookingService.validatePass(request.getPassCode(), shopId);
        
        if (response.isValid()) {
            return ResponseEntity.ok(ApiResponse.success(response.getMessage(), response));
        } else {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(response.getMessage()));
        }
    }

    /**
     * Confirm booking after payment (called by payment service).
     */
    @PostMapping("/{bookingId}/confirm")
    @Operation(summary = "Confirm booking after payment")
    public ResponseEntity<ApiResponse<BookingResponse>> confirmBooking(
            @PathVariable Long bookingId,
            @RequestParam String paymentId,
            @RequestParam String paymentOrderId) {
        
        BookingResponse booking = bookingService.confirmBooking(bookingId, paymentId, paymentOrderId);
        return ResponseEntity.ok(ApiResponse.success("Booking confirmed", booking));
    }

    /**
     * Update booking status.
     */
    @PatchMapping("/{bookingId}/status")
    @Operation(summary = "Update booking status")
    public ResponseEntity<ApiResponse<BookingResponse>> updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam BookingStatus status) {
        
        BookingResponse booking = bookingService.updateBookingStatus(bookingId, status);
        return ResponseEntity.ok(ApiResponse.success("Booking status updated", booking));
    }
}

