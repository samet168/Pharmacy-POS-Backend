package com.pharmacy.pos.inventory.repository;

import com.pharmacy.pos.inventory.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findByBranchId(Long branchId);
    List<StockMovement> findByBatchId(Long batchId);
    List<StockMovement> findByReferenceTableAndReferenceId(String referenceTable, Long referenceId);
}
