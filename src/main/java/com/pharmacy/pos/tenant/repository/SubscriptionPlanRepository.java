package com.pharmacy.pos.tenant.repository;

import com.pharmacy.pos.tenant.entity.SubscriptionPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
    Page<SubscriptionPlan> findByOrganizationId(Long organizationId, Pageable pageable);
    List<SubscriptionPlan> findByOrganizationId(Long organizationId);
}
