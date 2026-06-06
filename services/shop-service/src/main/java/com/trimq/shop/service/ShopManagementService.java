package com.trimq.shop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trimq.common.dto.PagedResponse;
import com.trimq.common.enums.ShopStatus;
import com.trimq.common.exception.BadRequestException;
import com.trimq.common.exception.ConflictException;
import com.trimq.common.exception.ForbiddenException;
import com.trimq.common.exception.ResourceNotFoundException;
import com.trimq.shop.dto.*;
import com.trimq.shop.entity.Shop;
import com.trimq.shop.entity.WorkingHours;
import com.trimq.shop.mapper.ShopMapper;
import com.trimq.shop.repository.ShopRepository;
import com.trimq.shop.repository.WorkingHoursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

/**
 * Service for shop management operations.
 * Handles shop CRUD and configuration.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShopManagementService {

    private final ShopRepository shopRepository;
    private final WorkingHoursRepository workingHoursRepository;
    private final ShopMapper shopMapper;
    private final ObjectMapper objectMapper;

    /**
     * Create a new shop.
     */
    @Transactional
    public ShopResponse createShop(String ownerId, ShopRequest request) {
        log.info("Creating shop for owner: {}", ownerId);

        UUID ownerUuid = UUID.fromString(ownerId);

        // Check for duplicate shop name by owner
        if (shopRepository.existsByOwnerIdAndNameIgnoreCase(ownerUuid, request.getName())) {
            throw new ConflictException("You already have a shop with this name");
        }

        Shop shop = Shop.builder()
                .ownerId(ownerUuid)
                .name(request.getName().trim())
                .description(request.getDescription())
                .phone(request.getPhone())
                .email(request.getEmail())
                .addressLine(request.getAddressLine())
                .area(request.getArea())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .images(serializeList(request.getImages()))
                .coverImage(request.getCoverImage())
                .slotDurationMinutes(request.getSlotDurationMinutes() != null ? request.getSlotDurationMinutes() : 30)
                .advanceBookingDays(request.getAdvanceBookingDays() != null ? request.getAdvanceBookingDays() : 7)
                .cancellationHours(request.getCancellationHours() != null ? request.getCancellationHours() : 2)
                .cancellationChargePercent(request.getCancellationChargePercent() != null ? request.getCancellationChargePercent() : 50)
                .status(ShopStatus.PENDING_APPROVAL)
                .build();

        shop = shopRepository.save(shop);

        // Create default working hours if provided
        if (request.getWorkingHours() != null && !request.getWorkingHours().isEmpty()) {
            createWorkingHours(shop, request.getWorkingHours());
        } else {
            createDefaultWorkingHours(shop);
        }

        log.info("Shop created successfully: {}", shop.getId());
        return shopMapper.toShopResponse(shop);
    }

    /**
     * Update shop details.
     */
    @Transactional
    public ShopResponse updateShop(String shopId, String userId, ShopRequest request) {
        log.info("Updating shop: {} by user: {}", shopId, userId);

        Shop shop = getShopWithOwnerCheck(shopId, userId);

        // Update fields
        if (request.getName() != null) shop.setName(request.getName().trim());
        if (request.getDescription() != null) shop.setDescription(request.getDescription());
        if (request.getPhone() != null) shop.setPhone(request.getPhone());
        if (request.getEmail() != null) shop.setEmail(request.getEmail());
        if (request.getAddressLine() != null) shop.setAddressLine(request.getAddressLine());
        if (request.getArea() != null) shop.setArea(request.getArea());
        if (request.getCity() != null) shop.setCity(request.getCity());
        if (request.getState() != null) shop.setState(request.getState());
        if (request.getPincode() != null) shop.setPincode(request.getPincode());
        if (request.getLatitude() != null) shop.setLatitude(request.getLatitude());
        if (request.getLongitude() != null) shop.setLongitude(request.getLongitude());
        if (request.getImages() != null) shop.setImages(serializeList(request.getImages()));
        if (request.getCoverImage() != null) shop.setCoverImage(request.getCoverImage());
        if (request.getSlotDurationMinutes() != null) shop.setSlotDurationMinutes(request.getSlotDurationMinutes());
        if (request.getAdvanceBookingDays() != null) shop.setAdvanceBookingDays(request.getAdvanceBookingDays());
        if (request.getCancellationHours() != null) shop.setCancellationHours(request.getCancellationHours());
        if (request.getCancellationChargePercent() != null) shop.setCancellationChargePercent(request.getCancellationChargePercent());

        shop = shopRepository.save(shop);

        // Update working hours if provided
        if (request.getWorkingHours() != null && !request.getWorkingHours().isEmpty()) {
            updateWorkingHours(shop, request.getWorkingHours());
        }

        log.info("Shop updated successfully: {}", shop.getId());
        return shopMapper.toShopResponse(shop);
    }

    /**
     * Get shop by ID.
     */
    public ShopResponse getShop(String shopId) {
        Shop shop = findShopById(shopId);
        ShopResponse response = shopMapper.toShopResponse(shop);
        
        // Include relations
        response.setServices(shopMapper.toServiceResponseList(shop.getServices()));
        response.setStaff(shopMapper.toStaffResponseList(shop.getStaffMembers()));
        response.setWorkingHours(shopMapper.toWorkingHoursResponseList(shop.getWorkingHours()));
        
        return response;
    }

    /**
     * Get shops by owner.
     */
    public List<ShopResponse> getShopsByOwner(String ownerId) {
        List<Shop> shops = shopRepository.findByOwnerIdAndIsActiveTrue(UUID.fromString(ownerId));
        return shops.stream().map(shopMapper::toShopResponse).toList();
    }

    /**
     * Delete (soft delete) shop.
     */
    @Transactional
    public void deleteShop(String shopId, String userId) {
        log.info("Deleting shop: {} by user: {}", shopId, userId);

        Shop shop = getShopWithOwnerCheck(shopId, userId);
        shop.setIsActive(false);
        shop.setStatus(ShopStatus.CLOSED);
        shopRepository.save(shop);

        log.info("Shop deleted successfully: {}", shopId);
    }

    /**
     * Activate shop (admin operation).
     */
    @Transactional
    public ShopResponse activateShop(String shopId) {
        Shop shop = findShopById(shopId);
        shop.setStatus(ShopStatus.ACTIVE);
        shop = shopRepository.save(shop);
        log.info("Shop activated: {}", shopId);
        return shopMapper.toShopResponse(shop);
    }

    /**
     * Search shops.
     */
    public PagedResponse<ShopResponse> searchShops(ShopSearchRequest request) {
        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? Math.min(request.getSize(), 50) : 10;
        
        Sort sort = buildSort(request.getSortBy(), request.getSortOrder());
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Shop> shopPage;

        if (request.getQuery() != null && !request.getQuery().isBlank()) {
            // Text search
            shopPage = shopRepository.searchByQuery(request.getQuery(), ShopStatus.ACTIVE, pageable);
        } else if (request.getCity() != null && request.getArea() != null) {
            // City + Area filter
            shopPage = shopRepository.findByCityIgnoreCaseAndAreaContainingIgnoreCaseAndStatusAndIsActiveTrue(
                    request.getCity(), request.getArea(), ShopStatus.ACTIVE, pageable);
        } else if (request.getCity() != null) {
            // City filter
            shopPage = shopRepository.findByCityIgnoreCaseAndStatusAndIsActiveTrue(
                    request.getCity(), ShopStatus.ACTIVE, pageable);
        } else {
            // All active shops
            shopPage = shopRepository.findByStatusAndIsActiveTrue(ShopStatus.ACTIVE, pageable);
        }

        List<ShopResponse> content = shopPage.getContent().stream()
                .map(shopMapper::toShopResponse)
                .toList();

        return PagedResponse.of(content, page, size, shopPage.getTotalElements(), shopPage.getTotalPages());
    }

    // ==================== Helper Methods ====================

    private Shop findShopById(String shopId) {
        return shopRepository.findByIdAndIsActiveTrue(UUID.fromString(shopId))
                .orElseThrow(() -> new ResourceNotFoundException("Shop", "id", shopId));
    }

    private Shop getShopWithOwnerCheck(String shopId, String userId) {
        Shop shop = findShopById(shopId);
        if (!shop.getOwnerId().toString().equals(userId)) {
            throw new ForbiddenException("You don't have permission to modify this shop");
        }
        return shop;
    }

    private void createDefaultWorkingHours(Shop shop) {
        for (DayOfWeek day : DayOfWeek.values()) {
            WorkingHours wh = WorkingHours.builder()
                    .shop(shop)
                    .dayOfWeek(day)
                    .isOpen(day != DayOfWeek.SUNDAY)
                    .openTime(java.time.LocalTime.of(9, 0))
                    .closeTime(java.time.LocalTime.of(21, 0))
                    .build();
            workingHoursRepository.save(wh);
        }
    }

    private void createWorkingHours(Shop shop, List<WorkingHoursRequest> requests) {
        for (WorkingHoursRequest req : requests) {
            WorkingHours wh = WorkingHours.builder()
                    .shop(shop)
                    .dayOfWeek(req.getDayOfWeek())
                    .isOpen(req.getIsOpen() != null ? req.getIsOpen() : true)
                    .openTime(req.getOpenTime())
                    .closeTime(req.getCloseTime())
                    .breakStart(req.getBreakStart())
                    .breakEnd(req.getBreakEnd())
                    .build();
            workingHoursRepository.save(wh);
        }
    }

    private void updateWorkingHours(Shop shop, List<WorkingHoursRequest> requests) {
        for (WorkingHoursRequest req : requests) {
            WorkingHours wh = workingHoursRepository
                    .findByShopIdAndDayOfWeek(shop.getId(), req.getDayOfWeek())
                    .orElse(WorkingHours.builder().shop(shop).dayOfWeek(req.getDayOfWeek()).build());

            wh.setIsOpen(req.getIsOpen() != null ? req.getIsOpen() : wh.getIsOpen());
            wh.setOpenTime(req.getOpenTime() != null ? req.getOpenTime() : wh.getOpenTime());
            wh.setCloseTime(req.getCloseTime() != null ? req.getCloseTime() : wh.getCloseTime());
            wh.setBreakStart(req.getBreakStart());
            wh.setBreakEnd(req.getBreakEnd());

            workingHoursRepository.save(wh);
        }
    }

    private String serializeList(List<String> list) {
        if (list == null || list.isEmpty()) return null;
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Invalid data format");
        }
    }

    private Sort buildSort(String sortBy, String sortOrder) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC;
        
        return switch (sortBy != null ? sortBy.toLowerCase() : "rating") {
            case "name" -> Sort.by(direction, "name");
            case "popularity" -> Sort.by(direction, "totalBookings");
            default -> Sort.by(Sort.Direction.DESC, "averageRating");
        };
    }
}

