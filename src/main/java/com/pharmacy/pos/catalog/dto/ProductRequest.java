package com.pharmacy.pos.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRequest {
    @NotNull
    private Long organizationId;

    @NotBlank
    private String sku;

    @NotBlank
    private String brandName;

    private Long genericNameId;

    private Long categoryId;

    private Long defaultSupplierId;

    private Boolean requiresPrescription;

    private Boolean isControlledSubstance;

    private Integer minStockAlert;

    private Boolean isActive;
}
