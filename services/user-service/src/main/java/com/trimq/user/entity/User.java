package com.trimq.user.entity;

import com.trimq.common.entity.BaseEntity;
import com.trimq.common.enums.Role;
import jakarta.persistence.*;
import lombok.*;

/**
 * User entity representing platform users.
 * Supports both customers and shop owners.
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_user_email", columnList = "email", unique = true),
    @Index(name = "idx_user_phone", columnList = "phone", unique = true),
    @Index(name = "idx_user_role", columnList = "role")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "phone", unique = true, nullable = false, length = 15)
    private String phone;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    @Builder.Default
    private Role role = Role.USER;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private Boolean emailVerified = false;

    @Column(name = "phone_verified", nullable = false)
    @Builder.Default
    private Boolean phoneVerified = false;

    @Column(name = "fcm_token", length = 500)
    private String fcmToken;

    @Column(name = "last_login_at")
    private java.time.LocalDateTime lastLoginAt;

    @Column(name = "failed_login_attempts")
    @Builder.Default
    private Integer failedLoginAttempts = 0;

    @Column(name = "account_locked", nullable = false)
    @Builder.Default
    private Boolean accountLocked = false;

    @Column(name = "lock_time")
    private java.time.LocalDateTime lockTime;

    /**
     * Check if account is locked and if lock has expired.
     */
    public boolean isAccountNonLocked() {
        if (!accountLocked) {
            return true;
        }
        // Unlock after 30 minutes
        if (lockTime != null && lockTime.plusMinutes(30).isBefore(java.time.LocalDateTime.now())) {
            this.accountLocked = false;
            this.failedLoginAttempts = 0;
            return true;
        }
        return false;
    }

    /**
     * Increment failed login attempts and lock if threshold reached.
     */
    public void incrementFailedAttempts() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= 5) {
            this.accountLocked = true;
            this.lockTime = java.time.LocalDateTime.now();
        }
    }

    /**
     * Reset failed attempts on successful login.
     */
    public void resetFailedAttempts() {
        this.failedLoginAttempts = 0;
        this.accountLocked = false;
        this.lockTime = null;
        this.lastLoginAt = java.time.LocalDateTime.now();
    }
}

