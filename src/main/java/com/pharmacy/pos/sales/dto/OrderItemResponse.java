package com.pharmacy.pos.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {

    private Long id;
    private Long orderId;
    private Long productId;
    private Long batchId;
    private Long unitId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private String dosageInstruction;
    private LocalDateTime createdAt;
}
