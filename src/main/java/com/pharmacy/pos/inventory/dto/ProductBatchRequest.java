package com.pharmacy.pos.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBatchRequest {
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @NotBlank(message = "Batch number is required")
    private String batchNumber;
    
    private LocalDate mfgDate;
    
    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;
    
    private Double costPrice;
    
    private Integer quantityReceived;
    
    private Integer quantityRemaining;
    
    private Long branchId;
}