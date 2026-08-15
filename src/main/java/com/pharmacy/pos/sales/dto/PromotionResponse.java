package com.pharmacy.pos.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionResponse {
    private Long id;
    private Long organizationId;
    private String name;
    private String code;
    private String description;
    private String promotionType;
    private BigDecimal discountValue;
    private BigDecimal minPurchaseAmount;
    private BigDecimal maxDiscountAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;
    private Integer usageLimit;
    private Integer usageCount;
    private String customerType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
