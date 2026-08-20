package com.pharmacy.pos.inventory.repository;

import com.pharmacy.pos.inventory.entity.StockAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockAdjustmentRepository extends JpaRepository<StockAdjustment, Long> {

    // branch and product are @ManyToOne
    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.branch.id = :branchId")
    List<StockAdjustment> findByBranchId(@Param("branchId") Long branchId);

    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.product.id = :productId")
    List<StockAdjustment> findByProductId(@Param("productId") Long productId);

    // reason is a plain @Column String — derived query is fine
    List<StockAdjustment> findByReason(String reason);
}
