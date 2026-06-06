package com.trimq.user.service;

import com.trimq.common.exception.ResourceNotFoundException;
import com.trimq.user.dto.AuthResponse;
import com.trimq.user.dto.UserProfileRequest;
import com.trimq.user.entity.User;
import com.trimq.user.mapper.UserMapper;
import com.trimq.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service for user profile operations.
 * Handles profile viewing and updates.
 * 
 * Follows Single Responsibility Principle - handles only user profile logic.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Get current user profile.
     */
    public AuthResponse.UserDto getProfile(String userId) {
        log.debug("Getting profile for user: {}", userId);
        
        User user = findUserById(userId);
        return userMapper.toUserDto(user);
    }

    /**
     * Update user profile.
     */
    @Transactional
    public AuthResponse.UserDto updateProfile(String userId, UserProfileRequest request) {
        log.info("Updating profile for user: {}", userId);

        User user = findUserById(userId);

        // Update fields if provided
        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName().trim());
        }

        if (request.getProfileImageUrl() != null) {
            user.setProfileImageUrl(request.getProfileImageUrl());
        }

        if (request.getFcmToken() != null) {
            user.setFcmToken(request.getFcmToken());
        }

        user = userRepository.save(user);
        log.info("Profile updated successfully for user: {}", userId);

        return userMapper.toUserDto(user);
    }

    /**
     * Get user by ID (for inter-service communication).
     */
    public User getUserById(String userId) {
        return findUserById(userId);
    }

    /**
     * Check if user exists.
     */
    public boolean userExists(String userId) {
        return userRepository.existsById(UUID.fromString(userId));
    }

    /**
     * Find user by ID or throw exception.
     */
    private User findUserById(String userId) {
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }
}

