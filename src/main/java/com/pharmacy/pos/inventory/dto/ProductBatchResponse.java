package com.pharmacy.pos.inventory.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductBatchResponse {
    
    private Long id;
    private Long productId;
    private String productName;
    private String productSku;
    private String batchNumber;
    private LocalDate mfgDate;
    private LocalDate expiryDate;
    private Double costPrice;
    private Integer quantityReceived;
    private Integer quantityRemaining;
    private Long branchId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}