package com.pharmacy.pos.branch.dto;

import com.pharmacy.pos.common.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchSettingsResponse {
    private Long id;
    private Long branchId;
    private BigDecimal taxRate;
    private String receiptHeader;
    private String receiptFooter;
    private boolean allowNegativeStock;
    private PaymentMethod defaultPaymentMethod;
    private LocalDateTime updatedAt;
}
