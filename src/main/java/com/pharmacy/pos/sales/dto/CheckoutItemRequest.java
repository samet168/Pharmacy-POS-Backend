package com.pharmacy.pos.sales.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutItemRequest {

    @NotNull
    private Long productId;

    @NotNull
    private Integer quantity;

    @NotNull
    private Long unitId;

    @NotNull
    private BigDecimal unitPrice;

    private String dosageInstruction;
}
