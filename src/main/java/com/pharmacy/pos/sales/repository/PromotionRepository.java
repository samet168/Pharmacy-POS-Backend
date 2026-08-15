package com.pharmacy.pos.sales.repository;

import com.pharmacy.pos.sales.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    List<Promotion> findByOrganizationId(Long organizationId);

    Optional<Promotion> findByCode(String code);

    List<Promotion> findByOrganizationIdAndIsActiveTrue(Long organizationId);

    List<Promotion> findByOrganizationIdAndIsActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long organizationId, LocalDate startDate, LocalDate endDate);
}
