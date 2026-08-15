package com.pharmacy.pos.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockAdjustmentRequest {
    @NotNull
    private Long branchId;

    @NotNull
    private Long productId;

    private Long batchId;

    @NotBlank
    private String adjustmentType;

    @NotNull
    private Integer quantityBefore;

    @NotNull
    private Integer quantityAfter;

    @NotNull
    private Integer adjustmentQuantity;

    @NotBlank
    private String reason;

    private String notes;

    private BigDecimal totalLoss;

    private Long approvedBy;

    @NotNull
    private Long performedBy;
}
