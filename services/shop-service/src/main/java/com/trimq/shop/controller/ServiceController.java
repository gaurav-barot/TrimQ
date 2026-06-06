package com.trimq.shop.controller;

import com.trimq.common.dto.ApiResponse;
import com.trimq.common.exception.UnauthorizedException;
import com.trimq.shop.dto.ServiceRequest;
import com.trimq.shop.dto.ServiceResponse;
import com.trimq.shop.service.ServiceManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for shop service (haircut, beard, etc.) operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
@Tag(name = "Service Management", description = "Shop services CRUD operations")
public class ServiceController {

    private final ServiceManagementService serviceManagementService;

    @PostMapping
    @Operation(summary = "Add a service", description = "Add a new service to a shop")
    public ResponseEntity<ApiResponse<ServiceResponse>> addService(
            @RequestParam String shopId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @Valid @RequestBody ServiceRequest request) {
        
        validateUserHeader(userId);
        log.info("Add service request to shop: {} by user: {}", shopId, userId);
        
        ServiceResponse response = serviceManagementService.addService(shopId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Service added successfully", response));
    }

    @GetMapping("/{shopId}")
    @Operation(summary = "Get services", description = "Get all services for a shop")
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> getServices(@PathVariable String shopId) {
        log.info("Get services request for shop: {}", shopId);
        List<ServiceResponse> response = serviceManagementService.getServices(shopId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{shopId}/available")
    @Operation(summary = "Get available services", description = "Get available services for booking")
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> getAvailableServices(@PathVariable String shopId) {
        log.info("Get available services request for shop: {}", shopId);
        List<ServiceResponse> response = serviceManagementService.getAvailableServices(shopId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{shopId}/category/{category}")
    @Operation(summary = "Get services by category", description = "Get services filtered by category")
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> getServicesByCategory(
            @PathVariable String shopId,
            @PathVariable String category) {
        log.info("Get services by category request - shop: {}, category: {}", shopId, category);
        List<ServiceResponse> response = serviceManagementService.getServicesByCategory(shopId, category);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{serviceId}")
    @Operation(summary = "Update a service", description = "Update service details")
    public ResponseEntity<ApiResponse<ServiceResponse>> updateService(
            @PathVariable String serviceId,
            @RequestParam String shopId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @Valid @RequestBody ServiceRequest request) {
        
        validateUserHeader(userId);
        log.info("Update service request: {} in shop: {}", serviceId, shopId);
        
        ServiceResponse response = serviceManagementService.updateService(shopId, serviceId, userId, request);
        return ResponseEntity.ok(ApiResponse.success("Service updated successfully", response));
    }

    @DeleteMapping("/{serviceId}")
    @Operation(summary = "Delete a service", description = "Remove a service from shop")
    public ResponseEntity<ApiResponse<Void>> deleteService(
            @PathVariable String serviceId,
            @RequestParam String shopId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        validateUserHeader(userId);
        log.info("Delete service request: {} from shop: {}", serviceId, shopId);
        
        serviceManagementService.deleteService(shopId, serviceId, userId);
        return ResponseEntity.ok(ApiResponse.success("Service deleted successfully"));
    }

    private void validateUserHeader(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new UnauthorizedException("User authentication required");
        }
    }
}

