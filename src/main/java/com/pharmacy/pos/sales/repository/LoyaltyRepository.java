package com.pharmacy.pos.sales.repository;

import com.pharmacy.pos.sales.entity.Loyalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoyaltyRepository extends JpaRepository<Loyalty, Long> {

    List<Loyalty> findByOrganizationId(Long organizationId);

    Optional<Loyalty> findByOrganizationIdAndIsActiveTrue(Long organizationId);
}
