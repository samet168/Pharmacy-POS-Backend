package com.pharmacy.pos.sales.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PromotionRequest {
    @NotNull
    private Long organizationId;

    @NotBlank
    private String name;

    private String code;

    private String description;

    @NotBlank
    private String promotionType;

    private BigDecimal discountValue;

    private BigDecimal minPurchaseAmount;

    private BigDecimal maxDiscountAmount;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private Boolean isActive = true;

    private Integer usageLimit;

    private String customerType;
}
