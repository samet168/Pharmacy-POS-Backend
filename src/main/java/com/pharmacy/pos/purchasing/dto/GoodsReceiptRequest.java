package com.pharmacy.pos.purchasing.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class GoodsReceiptRequest {
    @NotNull
    private Long purchaseOrderId;

    @NotNull
    private Long branchId;

    private Long receivedBy;

    private String notes;

    private List<GoodsReceiptItemRequest> items;
}
