package com.pharmacy.pos.inventory.dto;

import com.pharmacy.pos.common.enums.TransferStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockTransferResponse {
    private Long id;
    private Long fromBranchId;
    private String fromBranchName;
    private Long toBranchId;
    private String toBranchName;
    private Long batchId;
    private String batchNumber;
    private Long productId;
    private String productName;
    private Integer quantity;
    private TransferStatus status;
    private String notes;
    private Long requestedBy;
    private Long approvedBy;
    private LocalDateTime approvedAt;
    private Long receivedBy;
    private LocalDateTime receivedAt;
    private LocalDate estimatedArrival;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
