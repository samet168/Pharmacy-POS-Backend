package com.pharmacy.pos.inventory.repository;

import com.pharmacy.pos.inventory.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    // branch and batch are @ManyToOne
    @Query("SELECT sm FROM StockMovement sm WHERE sm.branch.id = :branchId")
    List<StockMovement> findByBranchId(@Param("branchId") Long branchId);

    @Query("SELECT sm FROM StockMovement sm WHERE sm.batch.id = :batchId")
    List<StockMovement> findByBatchId(@Param("batchId") Long batchId);

    // referenceTable and referenceId are plain @Column fields — derived query is fine
    List<StockMovement> findByReferenceTableAndReferenceId(String referenceTable, Long referenceId);
}
