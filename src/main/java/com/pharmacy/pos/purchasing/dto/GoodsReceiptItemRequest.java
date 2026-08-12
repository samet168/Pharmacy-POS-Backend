package com.pharmacy.pos.purchasing.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsReceiptItemRequest {
    @NotNull
    private Long productId;

    @NotNull
    private Long batchId;

    @NotNull
    private Long unitId;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal unitCost;
}
