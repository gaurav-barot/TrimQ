package com.trimq.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for shop search.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopSearchRequest {

    private String query;        // Text search
    private String city;         // Filter by city
    private String area;         // Filter by area
    
    // Location-based search
    private Double latitude;
    private Double longitude;
    private Double radiusKm;     // Search radius in km
    
    // Filters
    private BigDecimal minRating;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String category;     // Service category filter
    
    // Sorting
    private String sortBy;       // rating, distance, popularity, price
    private String sortOrder;    // asc, desc
    
    // Pagination
    private Integer page;
    private Integer size;
}

