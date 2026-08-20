package com.pharmacy.pos.purchasing.repository;

import com.pharmacy.pos.common.enums.PurchaseStatus;
import com.pharmacy.pos.purchasing.entity.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    // organization, branch, supplier are all @ManyToOne
    @Query("SELECT po FROM PurchaseOrder po WHERE po.organization.id = :orgId")
    Page<PurchaseOrder> findByOrganizationId(@Param("orgId") Long organizationId, Pageable pageable);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.branch.id = :branchId")
    Page<PurchaseOrder> findByBranchId(@Param("branchId") Long branchId, Pageable pageable);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.supplier.id = :supplierId")
    Page<PurchaseOrder> findBySupplierId(@Param("supplierId") Long supplierId, Pageable pageable);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.organization.id = :orgId AND po.status = :status")
    Page<PurchaseOrder> findByOrganizationIdAndStatus(
            @Param("orgId") Long organizationId,
            @Param("status") PurchaseStatus status,
            Pageable pageable);

    // poNumber is a plain @Column — derived query is fine
    boolean existsByPoNumber(String poNumber);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.status = :status AND po.organization.id = :orgId")
    List<PurchaseOrder> findByStatusAndOrganizationId(
            @Param("status") PurchaseStatus status,
            @Param("orgId") Long organizationId);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.organization.id = :orgId " +
           "AND po.createdAt BETWEEN :from AND :to")
    List<PurchaseOrder> findByOrganizationIdAndCreatedAtBetween(
            @Param("orgId") Long organizationId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}
