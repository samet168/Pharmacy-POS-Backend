package com.pharmacy.pos.inventory.repository;

import com.pharmacy.pos.inventory.entity.BranchInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchInventoryRepository extends JpaRepository<BranchInventory, Long> {
    @Query("SELECT bi FROM BranchInventory bi " +
           "WHERE bi.branch.id = :branchId AND bi.batch.id = :batchId")
    Optional<BranchInventory> findByBranchIdAndBatchId(
            @Param("branchId") Long branchId,
            @Param("batchId") Long batchId);

    @Query("SELECT bi FROM BranchInventory bi " +
           "JOIN bi.batch pb " +
           "WHERE bi.branch.id = :branchId AND pb.product.id = :productId " +
           "AND bi.quantityInBaseUnit > 0 " +
           "ORDER BY pb.expiryDate ASC")
    List<BranchInventory> findAvailableBatchesByBranchAndProduct(
            @Param("branchId") Long branchId,
            @Param("productId") Long productId);
}
