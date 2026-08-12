package com.pharmacy.pos.purchasing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItemResponse {
    private Long id;
    private Long purchaseOrderId;
    private Long productId;
    private Long unitId;
    private Integer quantity;
    private BigDecimal unitCost;
    private BigDecimal subtotal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
