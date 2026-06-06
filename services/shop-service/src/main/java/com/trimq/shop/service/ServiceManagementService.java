package com.trimq.shop.service;

import com.trimq.common.exception.ConflictException;
import com.trimq.common.exception.ForbiddenException;
import com.trimq.common.exception.ResourceNotFoundException;
import com.trimq.shop.dto.ServiceRequest;
import com.trimq.shop.dto.ServiceResponse;
import com.trimq.shop.entity.Shop;
import com.trimq.shop.entity.ShopService;
import com.trimq.shop.mapper.ShopMapper;
import com.trimq.shop.repository.ShopRepository;
import com.trimq.shop.repository.ShopServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing shop services (haircut, beard, etc.).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceManagementService {

    private final ShopRepository shopRepository;
    private final ShopServiceRepository shopServiceRepository;
    private final ShopMapper shopMapper;

    /**
     * Add a new service to shop.
     */
    @Transactional
    public ServiceResponse addService(String shopId, String userId, ServiceRequest request) {
        log.info("Adding service to shop: {} by user: {}", shopId, userId);

        Shop shop = getShopWithOwnerCheck(shopId, userId);

        // Check for duplicate service name
        if (shopServiceRepository.existsByShopIdAndNameIgnoreCaseAndIsActiveTrue(shop.getId(), request.getName())) {
            throw new ConflictException("Service with this name already exists");
        }

        ShopService service = ShopService.builder()
                .shop(shop)
                .name(request.getName().trim())
                .description(request.getDescription())
                .category(request.getCategory().toUpperCase())
                .price(request.getPrice())
                .durationMinutes(request.getDurationMinutes())
                .isAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true)
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .imageUrl(request.getImageUrl())
                .build();

        service = shopServiceRepository.save(service);
        log.info("Service added successfully: {}", service.getId());

        return shopMapper.toServiceResponse(service);
    }

    /**
     * Update a service.
     */
    @Transactional
    public ServiceResponse updateService(String shopId, String serviceId, String userId, ServiceRequest request) {
        log.info("Updating service: {} in shop: {}", serviceId, shopId);

        getShopWithOwnerCheck(shopId, userId);
        ShopService service = findServiceById(serviceId, shopId);

        if (request.getName() != null) service.setName(request.getName().trim());
        if (request.getDescription() != null) service.setDescription(request.getDescription());
        if (request.getCategory() != null) service.setCategory(request.getCategory().toUpperCase());
        if (request.getPrice() != null) service.setPrice(request.getPrice());
        if (request.getDurationMinutes() != null) service.setDurationMinutes(request.getDurationMinutes());
        if (request.getIsAvailable() != null) service.setIsAvailable(request.getIsAvailable());
        if (request.getDisplayOrder() != null) service.setDisplayOrder(request.getDisplayOrder());
        if (request.getImageUrl() != null) service.setImageUrl(request.getImageUrl());

        service = shopServiceRepository.save(service);
        log.info("Service updated successfully: {}", serviceId);

        return shopMapper.toServiceResponse(service);
    }

    /**
     * Get all services for a shop.
     */
    public List<ServiceResponse> getServices(String shopId) {
        List<ShopService> services = shopServiceRepository
                .findByShopIdAndIsActiveTrueOrderByDisplayOrderAsc(UUID.fromString(shopId));
        return shopMapper.toServiceResponseList(services);
    }

    /**
     * Get available services for a shop (for booking).
     */
    public List<ServiceResponse> getAvailableServices(String shopId) {
        List<ShopService> services = shopServiceRepository
                .findByShopIdAndIsAvailableTrueAndIsActiveTrueOrderByDisplayOrderAsc(UUID.fromString(shopId));
        return shopMapper.toServiceResponseList(services);
    }

    /**
     * Get services by category.
     */
    public List<ServiceResponse> getServicesByCategory(String shopId, String category) {
        List<ShopService> services = shopServiceRepository
                .findByShopIdAndCategoryAndIsActiveTrue(UUID.fromString(shopId), category.toUpperCase());
        return shopMapper.toServiceResponseList(services);
    }

    /**
     * Delete a service (soft delete).
     */
    @Transactional
    public void deleteService(String shopId, String serviceId, String userId) {
        log.info("Deleting service: {} from shop: {}", serviceId, shopId);

        getShopWithOwnerCheck(shopId, userId);
        ShopService service = findServiceById(serviceId, shopId);

        service.setIsActive(false);
        shopServiceRepository.save(service);

        log.info("Service deleted successfully: {}", serviceId);
    }

    // ==================== Helper Methods ====================

    private Shop getShopWithOwnerCheck(String shopId, String userId) {
        Shop shop = shopRepository.findByIdAndIsActiveTrue(UUID.fromString(shopId))
                .orElseThrow(() -> new ResourceNotFoundException("Shop", "id", shopId));
        
        if (!shop.getOwnerId().toString().equals(userId)) {
            throw new ForbiddenException("You don't have permission to modify this shop");
        }
        return shop;
    }

    private ShopService findServiceById(String serviceId, String shopId) {
        return shopServiceRepository.findByIdAndShopIdAndIsActiveTrue(
                UUID.fromString(serviceId), UUID.fromString(shopId))
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", serviceId));
    }
}

