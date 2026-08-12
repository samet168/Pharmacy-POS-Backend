package com.pharmacy.pos.sales.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderReturnItemRequest {

    @NotNull
    private Long orderReturnId;

    @NotNull
    private Long orderItemId;

    @NotNull
    private Integer quantity;

    private Boolean restock = true;
}
