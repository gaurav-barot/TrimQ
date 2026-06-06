package com.trimq.shop.entity;

import com.trimq.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Staff entity representing barbers/stylists working at a shop.
 */
@Entity
@Table(name = "staff", indexes = {
    @Index(name = "idx_staff_shop", columnList = "shop_id"),
    @Index(name = "idx_staff_user", columnList = "user_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Column(name = "user_id")
    private UUID userId; // Optional link to user account

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "role", nullable = false, length = 50)
    @Builder.Default
    private String role = "Stylist"; // Stylist, Senior Stylist, Junior, etc.

    @Column(name = "specializations", length = 500)
    private String specializations; // JSON array: ["Haircut", "Beard", "Fade"]

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @Column(name = "average_rating", precision = 2, scale = 1)
    @Builder.Default
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "total_reviews")
    @Builder.Default
    private Integer totalReviews = 0;

    @Column(name = "total_bookings")
    @Builder.Default
    private Integer totalBookings = 0;

    @Column(name = "is_available", nullable = false)
    @Builder.Default
    private Boolean isAvailable = true;

    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;
}

