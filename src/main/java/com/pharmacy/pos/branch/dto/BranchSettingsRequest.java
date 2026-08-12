package com.pharmacy.pos.branch.dto;

import com.pharmacy.pos.common.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BranchSettingsRequest {
    @NotNull
    private Long branchId;

    private BigDecimal taxRate;

    private String receiptHeader;

    private String receiptFooter;

    private Boolean allowNegativeStock;

    private PaymentMethod defaultPaymentMethod;
}
