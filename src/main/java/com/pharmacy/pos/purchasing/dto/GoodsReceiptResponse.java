package com.pharmacy.pos.purchasing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceiptResponse {
    private Long id;
    private Long purchaseOrderId;
    private Long branchId;
    private Long receivedBy;
    private LocalDateTime receivedAt;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<GoodsReceiptItemResponse> items;
}
