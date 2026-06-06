package com.trimq.shop.controller;

import com.trimq.common.dto.ApiResponse;
import com.trimq.common.dto.PagedResponse;
import com.trimq.common.exception.UnauthorizedException;
import com.trimq.shop.dto.ShopRequest;
import com.trimq.shop.dto.ShopResponse;
import com.trimq.shop.dto.ShopSearchRequest;
import com.trimq.shop.service.ShopManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST controller for shop operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/shops")
@RequiredArgsConstructor
@Tag(name = "Shop Management", description = "Shop CRUD and search operations")
public class ShopController {

    private final ShopManagementService shopManagementService;

    @PostMapping
    @Operation(summary = "Create a new shop", description = "Register a new salon/barber shop")
    public ResponseEntity<ApiResponse<ShopResponse>> createShop(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @Valid @RequestBody ShopRequest request) {
        
        validateUserHeader(userId);
        log.info("Create shop request from user: {}", userId);
        
        ShopResponse response = shopManagementService.createShop(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Shop created successfully. Pending approval.", response));
    }

    @GetMapping("/{shopId}")
    @Operation(summary = "Get shop details", description = "Get complete shop information including services and staff")
    public ResponseEntity<ApiResponse<ShopResponse>> getShop(@PathVariable String shopId) {
        log.info("Get shop request: {}", shopId);
        ShopResponse response = shopManagementService.getShop(shopId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{shopId}")
    @Operation(summary = "Update shop", description = "Update shop details (owner only)")
    public ResponseEntity<ApiResponse<ShopResponse>> updateShop(
            @PathVariable String shopId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @Valid @RequestBody ShopRequest request) {
        
        validateUserHeader(userId);
        log.info("Update shop request: {} by user: {}", shopId, userId);
        
        ShopResponse response = shopManagementService.updateShop(shopId, userId, request);
        return ResponseEntity.ok(ApiResponse.success("Shop updated successfully", response));
    }

    @DeleteMapping("/{shopId}")
    @Operation(summary = "Delete shop", description = "Soft delete a shop (owner only)")
    public ResponseEntity<ApiResponse<Void>> deleteShop(
            @PathVariable String shopId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        validateUserHeader(userId);
        log.info("Delete shop request: {} by user: {}", shopId, userId);
        
        shopManagementService.deleteShop(shopId, userId);
        return ResponseEntity.ok(ApiResponse.success("Shop deleted successfully"));
    }

    @GetMapping("/my-shops")
    @Operation(summary = "Get my shops", description = "Get all shops owned by current user")
    public ResponseEntity<ApiResponse<List<ShopResponse>>> getMyShops(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        validateUserHeader(userId);
        log.info("Get my shops request from user: {}", userId);
        
        List<ShopResponse> response = shopManagementService.getShopsByOwner(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search")
    @Operation(summary = "Search shops", description = "Search and filter shops")
    public ResponseEntity<ApiResponse<PagedResponse<ShopResponse>>> searchShops(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) BigDecimal minRating,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        log.info("Search shops request - query: {}, city: {}, area: {}", query, city, area);
        
        ShopSearchRequest request = ShopSearchRequest.builder()
                .query(query)
                .city(city)
                .area(area)
                .minRating(minRating)
                .sortBy(sortBy)
                .sortOrder(sortOrder)
                .page(page)
                .size(size)
                .build();
        
        PagedResponse<ShopResponse> response = shopManagementService.searchShops(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    private void validateUserHeader(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new UnauthorizedException("User authentication required");
        }
    }
}

