package com.trimq.shop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trimq.common.exception.BadRequestException;
import com.trimq.common.exception.ConflictException;
import com.trimq.common.exception.ForbiddenException;
import com.trimq.common.exception.ResourceNotFoundException;
import com.trimq.shop.dto.StaffRequest;
import com.trimq.shop.dto.StaffResponse;
import com.trimq.shop.entity.Shop;
import com.trimq.shop.entity.Staff;
import com.trimq.shop.mapper.ShopMapper;
import com.trimq.shop.repository.ShopRepository;
import com.trimq.shop.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing shop staff (barbers/stylists).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StaffManagementService {

    private final ShopRepository shopRepository;
    private final StaffRepository staffRepository;
    private final ShopMapper shopMapper;
    private final ObjectMapper objectMapper;

    /**
     * Add staff to shop.
     */
    @Transactional
    public StaffResponse addStaff(String shopId, String userId, StaffRequest request) {
        log.info("Adding staff to shop: {} by user: {}", shopId, userId);

        Shop shop = getShopWithOwnerCheck(shopId, userId);

        // Check for duplicate phone
        if (request.getPhone() != null && 
            staffRepository.existsByShopIdAndPhoneAndIsActiveTrue(shop.getId(), request.getPhone())) {
            throw new ConflictException("Staff with this phone number already exists");
        }

        Staff staff = Staff.builder()
                .shop(shop)
                .name(request.getName().trim())
                .phone(request.getPhone())
                .email(request.getEmail())
                .role(request.getRole())
                .specializations(serializeList(request.getSpecializations()))
                .profileImageUrl(request.getProfileImageUrl())
                .isAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true)
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .build();

        staff = staffRepository.save(staff);
        log.info("Staff added successfully: {}", staff.getId());

        return shopMapper.toStaffResponse(staff);
    }

    /**
     * Update staff details.
     */
    @Transactional
    public StaffResponse updateStaff(String shopId, String staffId, String userId, StaffRequest request) {
        log.info("Updating staff: {} in shop: {}", staffId, shopId);

        getShopWithOwnerCheck(shopId, userId);
        Staff staff = findStaffById(staffId, shopId);

        if (request.getName() != null) staff.setName(request.getName().trim());
        if (request.getPhone() != null) staff.setPhone(request.getPhone());
        if (request.getEmail() != null) staff.setEmail(request.getEmail());
        if (request.getRole() != null) staff.setRole(request.getRole());
        if (request.getSpecializations() != null) staff.setSpecializations(serializeList(request.getSpecializations()));
        if (request.getProfileImageUrl() != null) staff.setProfileImageUrl(request.getProfileImageUrl());
        if (request.getIsAvailable() != null) staff.setIsAvailable(request.getIsAvailable());
        if (request.getDisplayOrder() != null) staff.setDisplayOrder(request.getDisplayOrder());

        staff = staffRepository.save(staff);
        log.info("Staff updated successfully: {}", staffId);

        return shopMapper.toStaffResponse(staff);
    }

    /**
     * Get all staff for a shop.
     */
    public List<StaffResponse> getStaff(String shopId) {
        List<Staff> staff = staffRepository.findByShopIdAndIsActiveTrueOrderByDisplayOrderAsc(UUID.fromString(shopId));
        return shopMapper.toStaffResponseList(staff);
    }

    /**
     * Get available staff for booking.
     */
    public List<StaffResponse> getAvailableStaff(String shopId) {
        List<Staff> staff = staffRepository
                .findByShopIdAndIsAvailableTrueAndIsActiveTrueOrderByDisplayOrderAsc(UUID.fromString(shopId));
        return shopMapper.toStaffResponseList(staff);
    }

    /**
     * Delete staff (soft delete).
     */
    @Transactional
    public void deleteStaff(String shopId, String staffId, String userId) {
        log.info("Deleting staff: {} from shop: {}", staffId, shopId);

        getShopWithOwnerCheck(shopId, userId);
        Staff staff = findStaffById(staffId, shopId);

        staff.setIsActive(false);
        staffRepository.save(staff);

        log.info("Staff deleted successfully: {}", staffId);
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

    private Staff findStaffById(String staffId, String shopId) {
        return staffRepository.findByIdAndShopIdAndIsActiveTrue(
                UUID.fromString(staffId), UUID.fromString(shopId))
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", staffId));
    }

    private String serializeList(List<String> list) {
        if (list == null || list.isEmpty()) return null;
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Invalid data format");
        }
    }
}

