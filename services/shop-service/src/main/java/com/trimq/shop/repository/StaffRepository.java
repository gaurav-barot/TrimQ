package com.trimq.shop.repository;

import com.trimq.shop.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Staff entity operations.
 */
@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID> {

    List<Staff> findByShopIdAndIsActiveTrueOrderByDisplayOrderAsc(UUID shopId);

    List<Staff> findByShopIdAndIsAvailableTrueAndIsActiveTrueOrderByDisplayOrderAsc(UUID shopId);

    Optional<Staff> findByIdAndShopIdAndIsActiveTrue(UUID id, UUID shopId);

    Optional<Staff> findByUserIdAndIsActiveTrue(UUID userId);

    boolean existsByShopIdAndPhoneAndIsActiveTrue(UUID shopId, String phone);
}

