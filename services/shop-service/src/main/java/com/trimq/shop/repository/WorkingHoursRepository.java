package com.trimq.shop.repository;

import com.trimq.shop.entity.WorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for WorkingHours entity operations.
 */
@Repository
public interface WorkingHoursRepository extends JpaRepository<WorkingHours, UUID> {

    List<WorkingHours> findByShopIdOrderByDayOfWeek(UUID shopId);

    Optional<WorkingHours> findByShopIdAndDayOfWeek(UUID shopId, DayOfWeek dayOfWeek);

    void deleteByShopId(UUID shopId);
}

