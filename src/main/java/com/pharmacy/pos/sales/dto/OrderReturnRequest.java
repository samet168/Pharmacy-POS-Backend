package com.pharmacy.pos.sales.dto;

import com.pharmacy.pos.common.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderReturnRequest {

    @NotNull
    private Long orderId;

    private Long processedBy;

    private String reason;

    @NotNull
    private BigDecimal refundAmount;

    private PaymentMethod refundMethod;

    @NotNull
    @Valid
    @NotEmpty
    private List<OrderReturnItemRequest> items;
}
