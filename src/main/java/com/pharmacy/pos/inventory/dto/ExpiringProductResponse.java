package com.pharmacy.pos.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpiringProductResponse {
    private Long productId;
    private String productName;
    private String sku;
    private String batchNumber;
    private Integer quantity;
    private LocalDate expiryDate;
    private Integer daysUntilExpiry;
    private Long branchId;
    private String branchName;
}