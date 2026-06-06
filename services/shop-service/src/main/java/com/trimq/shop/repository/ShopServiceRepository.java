package com.trimq.shop.repository;

import com.trimq.shop.entity.ShopService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for ShopService entity operations.
 */
@Repository
public interface ShopServiceRepository extends JpaRepository<ShopService, UUID> {

    List<ShopService> findByShopIdAndIsActiveTrueOrderByDisplayOrderAsc(UUID shopId);

    List<ShopService> findByShopIdAndIsAvailableTrueAndIsActiveTrueOrderByDisplayOrderAsc(UUID shopId);

    List<ShopService> findByShopIdAndCategoryAndIsActiveTrue(UUID shopId, String category);

    Optional<ShopService> findByIdAndShopIdAndIsActiveTrue(UUID id, UUID shopId);

    boolean existsByShopIdAndNameIgnoreCaseAndIsActiveTrue(UUID shopId, String name);
}

