package com.pharmacy.pos.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUnitResponse {
    private Long id;
    private Long productId;
    private String unitName;
    private String barcode;
    private int conversionFactor;
    private boolean isBaseUnit;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private LocalDateTime createdAt;
}
