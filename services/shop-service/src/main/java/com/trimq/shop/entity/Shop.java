package com.trimq.shop.entity;

import com.trimq.common.entity.BaseEntity;
import com.trimq.common.enums.ShopStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Shop entity representing a salon/barber shop.
 */
@Entity
@Table(name = "shops", indexes = {
    @Index(name = "idx_shop_owner", columnList = "owner_id"),
    @Index(name = "idx_shop_city", columnList = "city"),
    @Index(name = "idx_shop_city_area", columnList = "city, area"),
    @Index(name = "idx_shop_status", columnList = "status"),
    @Index(name = "idx_shop_rating", columnList = "average_rating DESC")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shop extends BaseEntity {

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    // Address fields
    @Column(name = "address_line", nullable = false, length = 200)
    private String addressLine;

    @Column(name = "area", nullable = false, length = 100)
    private String area;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "state", nullable = false, length = 50)
    private String state;

    @Column(name = "pincode", nullable = false, length = 6)
    private String pincode;

    // Location for distance calculation
    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    // Images (stored as JSON array of URLs)
    @Column(name = "images", columnDefinition = "TEXT")
    private String images; // JSON array: ["url1", "url2"]

    @Column(name = "cover_image", length = 500)
    private String coverImage;

    // Rating
    @Column(name = "average_rating", precision = 2, scale = 1)
    @Builder.Default
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "total_reviews")
    @Builder.Default
    private Integer totalReviews = 0;

    @Column(name = "total_bookings")
    @Builder.Default
    private Integer totalBookings = 0;

    // Status
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ShopStatus status = ShopStatus.PENDING_APPROVAL;

    // Configuration
    @Column(name = "slot_duration_minutes")
    @Builder.Default
    private Integer slotDurationMinutes = 30;

    @Column(name = "advance_booking_days")
    @Builder.Default
    private Integer advanceBookingDays = 7;

    @Column(name = "cancellation_hours")
    @Builder.Default
    private Integer cancellationHours = 2;

    @Column(name = "cancellation_charge_percent")
    @Builder.Default
    private Integer cancellationChargePercent = 50;

    // Relations
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ShopService> services = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Staff> staffMembers = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<WorkingHours> workingHours = new ArrayList<>();
}

