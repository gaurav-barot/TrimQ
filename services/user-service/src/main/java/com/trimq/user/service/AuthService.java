package com.trimq.user.service;

import com.trimq.common.exception.BadRequestException;
import com.trimq.common.exception.ConflictException;
import com.trimq.common.exception.ResourceNotFoundException;
import com.trimq.common.exception.UnauthorizedException;
import com.trimq.user.dto.*;
import com.trimq.user.entity.User;
import com.trimq.user.mapper.UserMapper;
import com.trimq.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for authentication operations.
 * Handles registration, login, OTP verification, and password reset.
 * 
 * Follows Single Responsibility Principle - handles only auth logic.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;
    private final UserMapper userMapper;

    /**
     * Register a new user.
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email is already registered");
        }

        // Check if phone already exists
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new ConflictException("Phone number is already registered");
        }

        // Create user
        User user = User.builder()
                .email(request.getEmail().toLowerCase().trim())
                .phone(request.getPhone())
                .fullName(request.getFullName().trim())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .emailVerified(false)
                .phoneVerified(false)
                .build();

        user = userRepository.save(user);
        log.info("User registered successfully: {}", user.getId());

        // Send OTP for phone verification
        otpService.sendOtp(user.getPhone(), "REGISTRATION");

        // Generate tokens
        return buildAuthResponse(user);
    }

    /**
     * Login user with email/phone and password.
     */
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for: {}", request.getIdentifier());

        // Find user by email or phone
        User user = userRepository.findByEmailOrPhone(
                request.getIdentifier().toLowerCase(),
                request.getIdentifier()
        ).orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        // Check if account is locked
        if (!user.isAccountNonLocked()) {
            throw new UnauthorizedException("Account is locked. Please try again later.");
        }

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            user.incrementFailedAttempts();
            userRepository.save(user);
            throw new UnauthorizedException("Invalid credentials");
        }

        // Reset failed attempts and update last login
        user.resetFailedAttempts();
        userRepository.save(user);

        log.info("User logged in successfully: {}", user.getId());
        return buildAuthResponse(user);
    }

    /**
     * Request OTP for phone verification or login.
     */
    public void requestOtp(OtpRequest request) {
        log.info("OTP requested for phone: {}****{}", 
                request.getPhone().substring(0, 3), 
                request.getPhone().substring(7));

        // Verify phone exists for login/password reset
        if (!"REGISTRATION".equals(request.getPurpose())) {
            userRepository.findByPhone(request.getPhone())
                    .orElseThrow(() -> new ResourceNotFoundException("Phone number not registered"));
        }

        otpService.sendOtp(request.getPhone(), request.getPurpose());
    }

    /**
     * Verify OTP and mark phone as verified.
     */
    @Transactional
    public AuthResponse verifyOtp(OtpVerifyRequest request) {
        log.info("Verifying OTP for phone: {}", request.getPhone());

        if (!otpService.verifyOtp(request.getPhone(), request.getOtp())) {
            throw new BadRequestException("Invalid or expired OTP");
        }

        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Mark phone as verified
        user.setPhoneVerified(true);
        userRepository.save(user);

        log.info("Phone verified successfully for user: {}", user.getId());
        return buildAuthResponse(user);
    }

    /**
     * Refresh access token using refresh token.
     */
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        log.info("Token refresh requested");

        if (!jwtService.validateRefreshToken(request.getRefreshToken())) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        String userId = jwtService.extractUserId(request.getRefreshToken());
        User user = userRepository.findById(java.util.UUID.fromString(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        log.info("Token refreshed for user: {}", user.getId());
        return buildAuthResponse(user);
    }

    /**
     * Request password reset OTP.
     */
    public void requestPasswordReset(String phone) {
        log.info("Password reset requested for phone: {}", phone);

        userRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Phone number not registered"));

        otpService.sendOtp(phone, "PASSWORD_RESET");
    }

    /**
     * Reset password with OTP verification.
     */
    @Transactional
    public void resetPassword(PasswordResetRequest request) {
        log.info("Password reset for phone: {}", request.getPhone());

        if (!otpService.verifyOtp(request.getPhone(), request.getOtp())) {
            throw new BadRequestException("Invalid or expired OTP");
        }

        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.resetFailedAttempts();
        userRepository.save(user);

        log.info("Password reset successfully for user: {}", user.getId());
    }

    /**
     * Build authentication response with tokens.
     */
    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationSeconds())
                .user(userMapper.toUserDto(user))
                .build();
    }
}

