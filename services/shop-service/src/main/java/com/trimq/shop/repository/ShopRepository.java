package com.trimq.shop.repository;

import com.trimq.common.enums.ShopStatus;
import com.trimq.shop.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Shop entity operations.
 */
@Repository
public interface ShopRepository extends JpaRepository<Shop, UUID> {

    Optional<Shop> findByIdAndIsActiveTrue(UUID id);

    List<Shop> findByOwnerIdAndIsActiveTrue(UUID ownerId);

    Page<Shop> findByStatusAndIsActiveTrue(ShopStatus status, Pageable pageable);

    // Search by city
    Page<Shop> findByCityIgnoreCaseAndStatusAndIsActiveTrue(
            String city, ShopStatus status, Pageable pageable);

    // Search by city and area
    Page<Shop> findByCityIgnoreCaseAndAreaContainingIgnoreCaseAndStatusAndIsActiveTrue(
            String city, String area, ShopStatus status, Pageable pageable);

    // Text search on name or area
    @Query("SELECT s FROM Shop s WHERE s.status = :status AND s.isActive = true " +
           "AND (LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(s.area) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(s.city) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Shop> searchByQuery(@Param("query") String query, 
                             @Param("status") ShopStatus status, 
                             Pageable pageable);

    // Search with rating filter
    @Query("SELECT s FROM Shop s WHERE s.city = :city AND s.status = :status " +
           "AND s.isActive = true AND s.averageRating >= :minRating")
    Page<Shop> findByCityAndMinRating(@Param("city") String city,
                                       @Param("status") ShopStatus status,
                                       @Param("minRating") BigDecimal minRating,
                                       Pageable pageable);

    // Location-based search (simplified - for production use PostGIS)
    @Query("SELECT s FROM Shop s WHERE s.status = :status AND s.isActive = true " +
           "AND s.latitude IS NOT NULL AND s.longitude IS NOT NULL " +
           "AND s.latitude BETWEEN :minLat AND :maxLat " +
           "AND s.longitude BETWEEN :minLon AND :maxLon")
    Page<Shop> findByLocationBounds(@Param("minLat") Double minLat,
                                     @Param("maxLat") Double maxLat,
                                     @Param("minLon") Double minLon,
                                     @Param("maxLon") Double maxLon,
                                     @Param("status") ShopStatus status,
                                     Pageable pageable);

    boolean existsByOwnerIdAndNameIgnoreCase(UUID ownerId, String name);
}

