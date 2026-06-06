package com.trimq.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response DTO for shop service.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {

    private String id;
    private String shopId;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private Integer durationMinutes;
    private Boolean isAvailable;
    private Integer displayOrder;
    private String imageUrl;
}

