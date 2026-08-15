package com.pharmacy.pos.inventory.repository;

import com.pharmacy.pos.inventory.entity.StockTransfer;
import com.pharmacy.pos.common.enums.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {

    List<StockTransfer> findByFromBranchId(Long fromBranchId);

    List<StockTransfer> findByToBranchId(Long toBranchId);

    List<StockTransfer> findByStatus(TransferStatus status);

    List<StockTransfer> findByFromBranchIdOrToBranchId(Long fromBranchId, Long toBranchId);
}
