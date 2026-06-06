package com.trimq.shop.entity;

import com.trimq.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * ShopService entity representing services offered by a shop.
 * Examples: Haircut, Beard Trim, Face Pack, etc.
 */
@Entity
@Table(name = "shop_services", indexes = {
    @Index(name = "idx_service_shop", columnList = "shop_id"),
    @Index(name = "idx_service_category", columnList = "category")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopService extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "category", nullable = false, length = 50)
    private String category; // HAIRCUT, BEARD, SPA, etc.

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "is_available", nullable = false)
    @Builder.Default
    private Boolean isAvailable = true;

    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(name = "image_url", length = 500)
    private String imageUrl;
}

