package com.pharmacy.pos.sales.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private Long productId;

    @NotNull
    private Long batchId;

    @NotNull
    private Long unitId;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal unitPrice;

    @NotNull
    private BigDecimal subtotal;

    private String dosageInstruction;
}
