package com.pharmacy.pos.sales.repository;

import com.pharmacy.pos.sales.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.order.id = :orderId")
    List<Payment> findByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT p FROM Payment p WHERE p.order.id IN :orderIds")
    List<Payment> findByOrderIdIn(@Param("orderIds") List<Long> orderIds);

    @Query("SELECT p FROM Payment p WHERE p.order.organization.id = :organizationId")
    Page<Payment> findByOrderOrganizationId(@Param("organizationId") Long organizationId, Pageable pageable);

    @Query("SELECT p FROM Payment p WHERE p.order.organization.id = :organizationId AND p.order.branch.id = :branchId")
    Page<Payment> findByOrderOrganizationIdAndOrderBranchId(
            @Param("organizationId") Long organizationId,
            @Param("branchId") Long branchId,
            Pageable pageable);
}
