package com.pharmacy.pos.inventory.repository;

import com.pharmacy.pos.inventory.entity.StockAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockAdjustmentRepository extends JpaRepository<StockAdjustment, Long> {

    List<StockAdjustment> findByBranchId(Long branchId);

    List<StockAdjustment> findByProductId(Long productId);

    List<StockAdjustment> findByReason(String reason);
}
