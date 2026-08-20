package com.pharmacy.pos.inventory.repository;

import com.pharmacy.pos.common.enums.TransferStatus;
import com.pharmacy.pos.inventory.entity.StockTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {

    // fromBranch and toBranch are @ManyToOne
    @Query("SELECT st FROM StockTransfer st WHERE st.fromBranch.id = :branchId")
    List<StockTransfer> findByFromBranchId(@Param("branchId") Long fromBranchId);

    @Query("SELECT st FROM StockTransfer st WHERE st.toBranch.id = :branchId")
    List<StockTransfer> findByToBranchId(@Param("branchId") Long toBranchId);

    // status is an @Enumerated plain column — derived query is fine
    List<StockTransfer> findByStatus(TransferStatus status);

    @Query("SELECT st FROM StockTransfer st WHERE st.fromBranch.id = :fromId OR st.toBranch.id = :toId")
    List<StockTransfer> findByFromBranchIdOrToBranchId(
            @Param("fromId") Long fromBranchId,
            @Param("toId") Long toBranchId);
}
