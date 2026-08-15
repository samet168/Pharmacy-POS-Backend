package com.pharmacy.pos.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUnitRequest {
    @NotNull
    private Long productId;

    @NotBlank
    private String unitName;

    private String barcode;

    @NotNull
    private Integer conversionFactor;

    private Boolean isBaseUnit = false;

    private BigDecimal costPrice;

    private BigDecimal sellingPrice;
}
