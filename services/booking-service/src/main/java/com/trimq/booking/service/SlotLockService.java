package com.trimq.booking.service;

import com.trimq.booking.config.BookingConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Service for managing slot locks using Redis.
 * 
 * Prevents double booking by temporarily locking slots
 * when a user starts the booking process.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SlotLockService {

    private final StringRedisTemplate redisTemplate;
    private final BookingConfig bookingConfig;

    private static final String SLOT_LOCK_PREFIX = "slot:lock:";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HHmm");

    /**
     * Try to acquire a lock for a slot.
     * 
     * @param shopId Shop ID
     * @param date Booking date
     * @param startTime Slot start time
     * @param userId User trying to lock
     * @return Lock ID if successful, null otherwise
     */
    public String tryLock(Long shopId, LocalDate date, LocalTime startTime, Long userId) {
        String key = buildLockKey(shopId, date, startTime);
        String lockId = UUID.randomUUID().toString();
        String value = userId + ":" + lockId;

        Boolean acquired = redisTemplate.opsForValue()
                .setIfAbsent(key, value, Duration.ofSeconds(bookingConfig.getSlotLockTtlSeconds()));

        if (Boolean.TRUE.equals(acquired)) {
            log.info("Slot lock acquired: shop={}, date={}, time={}, user={}, lockId={}", 
                    shopId, date, startTime, userId, lockId);
            return lockId;
        }

        log.debug("Slot lock failed (already locked): shop={}, date={}, time={}", 
                shopId, date, startTime);
        return null;
    }

    /**
     * Release a slot lock.
     * 
     * @param shopId Shop ID
     * @param date Booking date
     * @param startTime Slot start time
     * @param lockId Lock ID to release
     * @return true if released, false otherwise
     */
    public boolean releaseLock(Long shopId, LocalDate date, LocalTime startTime, String lockId) {
        String key = buildLockKey(shopId, date, startTime);
        String value = redisTemplate.opsForValue().get(key);

        if (value != null && value.endsWith(":" + lockId)) {
            Boolean deleted = redisTemplate.delete(key);
            log.info("Slot lock released: shop={}, date={}, time={}, lockId={}", 
                    shopId, date, startTime, lockId);
            return Boolean.TRUE.equals(deleted);
        }

        log.warn("Failed to release lock (mismatch or expired): shop={}, date={}, time={}", 
                shopId, date, startTime);
        return false;
    }

    /**
     * Check if a slot is locked.
     * 
     * @param shopId Shop ID
     * @param date Booking date
     * @param startTime Slot start time
     * @return true if locked, false otherwise
     */
    public boolean isLocked(Long shopId, LocalDate date, LocalTime startTime) {
        String key = buildLockKey(shopId, date, startTime);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * Check if slot is locked by a specific user.
     * 
     * @param shopId Shop ID
     * @param date Booking date
     * @param startTime Slot start time
     * @param userId User to check
     * @return true if locked by user, false otherwise
     */
    public boolean isLockedByUser(Long shopId, LocalDate date, LocalTime startTime, Long userId) {
        String key = buildLockKey(shopId, date, startTime);
        String value = redisTemplate.opsForValue().get(key);
        return value != null && value.startsWith(userId + ":");
    }

    /**
     * Get remaining TTL for a lock.
     * 
     * @param shopId Shop ID
     * @param date Booking date
     * @param startTime Slot start time
     * @return TTL in seconds, -1 if not found
     */
    public long getRemainingTtl(Long shopId, LocalDate date, LocalTime startTime) {
        String key = buildLockKey(shopId, date, startTime);
        Long ttl = redisTemplate.getExpire(key);
        return ttl != null ? ttl : -1;
    }

    /**
     * Extend lock TTL (refresh).
     * 
     * @param shopId Shop ID
     * @param date Booking date
     * @param startTime Slot start time
     * @param lockId Lock ID
     * @return true if extended, false otherwise
     */
    public boolean extendLock(Long shopId, LocalDate date, LocalTime startTime, String lockId) {
        String key = buildLockKey(shopId, date, startTime);
        String value = redisTemplate.opsForValue().get(key);

        if (value != null && value.endsWith(":" + lockId)) {
            Boolean success = redisTemplate.expire(key, 
                    Duration.ofSeconds(bookingConfig.getSlotLockTtlSeconds()));
            log.debug("Slot lock extended: shop={}, date={}, time={}", shopId, date, startTime);
            return Boolean.TRUE.equals(success);
        }

        return false;
    }

    private String buildLockKey(Long shopId, LocalDate date, LocalTime startTime) {
        return SLOT_LOCK_PREFIX + shopId + ":" + 
               date.format(DATE_FORMAT) + ":" + 
               startTime.format(TIME_FORMAT);
    }
}

