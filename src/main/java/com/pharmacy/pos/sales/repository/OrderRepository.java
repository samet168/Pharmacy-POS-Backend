package com.pharmacy.pos.sales.repository;

import com.pharmacy.pos.sales.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.organization.id = :organizationId")
    Page<Order> findByOrganizationId(@Param("organizationId") Long organizationId, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.branch.id = :branchId")
    Page<Order> findByBranchId(@Param("branchId") Long branchId, Pageable pageable);

    Optional<Order> findByInvoiceNumber(String invoiceNumber);

    Optional<Order> findByClientUuid(String clientUuid);

    @Query("SELECT o FROM Order o WHERE o.organization.id = :orgId AND o.branch.id = :branchId")
    Page<Order> findByOrganizationAndBranch(@Param("orgId") Long orgId, @Param("branchId") Long branchId, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.organization.id = :organizationId AND o.createdAt BETWEEN :from AND :to")
    List<Order> findByOrganizationIdAndCreatedAtBetween(@Param("organizationId") Long organizationId, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
