package com.pharmacy.pos.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockAdjustmentResponse {
    private Long id;
    private Long branchId;
    private String branchName;
    private Long productId;
    private String productName;
    private Long batchId;
    private String batchNumber;
    private String adjustmentType;
    private Integer quantityBefore;
    private Integer quantityAfter;
    private Integer adjustmentQuantity;
    private String reason;
    private String notes;
    private BigDecimal totalLoss;
    private Long approvedBy;
    private LocalDateTime approvedAt;
    private Long performedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
