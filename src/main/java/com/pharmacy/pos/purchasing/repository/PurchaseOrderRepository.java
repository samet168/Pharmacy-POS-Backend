package com.pharmacy.pos.purchasing.repository;

import com.pharmacy.pos.common.enums.PurchaseStatus;
import com.pharmacy.pos.purchasing.entity.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    Page<PurchaseOrder> findByOrganizationId(Long organizationId, Pageable pageable);
    Page<PurchaseOrder> findByBranchId(Long branchId, Pageable pageable);
    Page<PurchaseOrder> findBySupplierId(Long supplierId, Pageable pageable);
    Page<PurchaseOrder> findByOrganizationIdAndStatus(Long organizationId, PurchaseStatus status, Pageable pageable);
    boolean existsByPoNumber(String poNumber);
    List<PurchaseOrder> findByStatusAndOrganizationId(PurchaseStatus status, Long organizationId);
}
