package com.trimq.shop.dto;

import com.trimq.common.enums.ShopStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response DTO for shop details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopResponse {

    private String id;
    private String ownerId;
    private String name;
    private String description;
    private String phone;
    private String email;
    
    // Address
    private String addressLine;
    private String area;
    private String city;
    private String state;
    private String pincode;
    private String fullAddress;
    
    // Location
    private Double latitude;
    private Double longitude;
    private Double distanceKm; // Calculated if search with location
    
    // Images
    private List<String> images;
    private String coverImage;
    
    // Stats
    private BigDecimal averageRating;
    private Integer totalReviews;
    private Integer totalBookings;
    
    // Status
    private ShopStatus status;
    
    // Configuration
    private Integer slotDurationMinutes;
    private Integer advanceBookingDays;
    private Integer cancellationHours;
    private Integer cancellationChargePercent;
    
    // Relations (optional, based on query)
    private List<ServiceResponse> services;
    private List<StaffResponse> staff;
    private List<WorkingHoursResponse> workingHours;
    
    // Open status (calculated)
    private Boolean isOpenNow;
    private String openingTime;
    private String closingTime;
}

