package com.pharmacy.pos.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private Long organizationId;
    private String sku;
    private String brandName;
    private Long genericNameId;
    private Long categoryId;
    private Long defaultSupplierId;
    private boolean requiresPrescription;
    private boolean isControlledSubstance;
    private int minStockAlert;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}