package com.pharmacy.pos.purchasing.dto;

import com.pharmacy.pos.common.enums.PurchaseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PurchaseOrderRequest {
    @NotNull
    private Long organizationId;

    @NotNull
    private Long branchId;

    @NotNull
    private Long supplierId;

    @NotBlank
    private String poNumber;

    private PurchaseStatus status;

    private BigDecimal totalAmount;

    private Long createdBy;

    private List<PurchaseOrderItemRequest> items;
}
