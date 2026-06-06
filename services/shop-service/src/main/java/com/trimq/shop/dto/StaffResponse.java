package com.trimq.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response DTO for staff member.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffResponse {

    private String id;
    private String shopId;
    private String userId;
    private String name;
    private String phone;
    private String email;
    private String role;
    private List<String> specializations;
    private String profileImageUrl;
    private BigDecimal averageRating;
    private Integer totalReviews;
    private Integer totalBookings;
    private Boolean isAvailable;
    private Integer displayOrder;
}

