package com.trimq.shop.controller;

import com.trimq.common.dto.ApiResponse;
import com.trimq.common.exception.UnauthorizedException;
import com.trimq.shop.dto.StaffRequest;
import com.trimq.shop.dto.StaffResponse;
import com.trimq.shop.service.StaffManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for staff management operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
@Tag(name = "Staff Management", description = "Shop staff CRUD operations")
public class StaffController {

    private final StaffManagementService staffManagementService;

    @PostMapping
    @Operation(summary = "Add staff", description = "Add a new staff member to a shop")
    public ResponseEntity<ApiResponse<StaffResponse>> addStaff(
            @RequestParam String shopId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @Valid @RequestBody StaffRequest request) {
        
        validateUserHeader(userId);
        log.info("Add staff request to shop: {} by user: {}", shopId, userId);
        
        StaffResponse response = staffManagementService.addStaff(shopId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Staff added successfully", response));
    }

    @GetMapping("/{shopId}")
    @Operation(summary = "Get staff", description = "Get all staff for a shop")
    public ResponseEntity<ApiResponse<List<StaffResponse>>> getStaff(@PathVariable String shopId) {
        log.info("Get staff request for shop: {}", shopId);
        List<StaffResponse> response = staffManagementService.getStaff(shopId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{shopId}/available")
    @Operation(summary = "Get available staff", description = "Get available staff for booking")
    public ResponseEntity<ApiResponse<List<StaffResponse>>> getAvailableStaff(@PathVariable String shopId) {
        log.info("Get available staff request for shop: {}", shopId);
        List<StaffResponse> response = staffManagementService.getAvailableStaff(shopId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{staffId}")
    @Operation(summary = "Update staff", description = "Update staff member details")
    public ResponseEntity<ApiResponse<StaffResponse>> updateStaff(
            @PathVariable String staffId,
            @RequestParam String shopId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @Valid @RequestBody StaffRequest request) {
        
        validateUserHeader(userId);
        log.info("Update staff request: {} in shop: {}", staffId, shopId);
        
        StaffResponse response = staffManagementService.updateStaff(shopId, staffId, userId, request);
        return ResponseEntity.ok(ApiResponse.success("Staff updated successfully", response));
    }

    @DeleteMapping("/{staffId}")
    @Operation(summary = "Delete staff", description = "Remove a staff member from shop")
    public ResponseEntity<ApiResponse<Void>> deleteStaff(
            @PathVariable String staffId,
            @RequestParam String shopId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        validateUserHeader(userId);
        log.info("Delete staff request: {} from shop: {}", staffId, shopId);
        
        staffManagementService.deleteStaff(shopId, staffId, userId);
        return ResponseEntity.ok(ApiResponse.success("Staff deleted successfully"));
    }

    private void validateUserHeader(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new UnauthorizedException("User authentication required");
        }
    }
}

