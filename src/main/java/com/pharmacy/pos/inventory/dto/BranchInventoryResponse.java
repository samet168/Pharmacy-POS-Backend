package com.pharmacy.pos.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchInventoryResponse {
    private Long id;
    private Long branchId;
    private String branchName;
    private Long batchId;
    private String batchNumber;
    private Long productId;
    private String productName;
    private Integer quantityInBaseUnit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
