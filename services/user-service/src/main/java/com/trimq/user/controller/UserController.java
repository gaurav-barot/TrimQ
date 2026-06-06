package com.trimq.user.controller;

import com.trimq.common.dto.ApiResponse;
import com.trimq.common.exception.UnauthorizedException;
import com.trimq.user.dto.AuthResponse;
import com.trimq.user.dto.UserProfileRequest;
import com.trimq.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for user profile endpoints.
 * All endpoints require JWT authentication (validated by API Gateway).
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "User profile management")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile", description = "Returns the authenticated user's profile")
    public ResponseEntity<ApiResponse<AuthResponse.UserDto>> getProfile(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        validateUserHeader(userId);
        log.info("Get profile request for user: {}", userId);
        
        AuthResponse.UserDto profile = userService.getProfile(userId);
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user profile", description = "Updates the authenticated user's profile")
    public ResponseEntity<ApiResponse<AuthResponse.UserDto>> updateProfile(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @Valid @RequestBody UserProfileRequest request) {
        
        validateUserHeader(userId);
        log.info("Update profile request for user: {}", userId);
        
        AuthResponse.UserDto profile = userService.updateProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", profile));
    }

    /**
     * Validate that user ID header is present.
     * This header is set by API Gateway after JWT validation.
     */
    private void validateUserHeader(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new UnauthorizedException("User authentication required");
        }
    }
}

