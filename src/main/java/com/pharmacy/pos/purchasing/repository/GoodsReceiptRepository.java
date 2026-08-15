package com.pharmacy.pos.purchasing.repository;

import com.pharmacy.pos.purchasing.entity.GoodsReceipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsReceiptRepository extends JpaRepository<GoodsReceipt, Long> {
    Page<GoodsReceipt> findByPurchaseOrderId(Long purchaseOrderId, Pageable pageable);
    Page<GoodsReceipt> findByBranchId(Long branchId, Pageable pageable);
    Page<GoodsReceipt> findByBranchOrganizationId(Long organizationId, Pageable pageable);
}
