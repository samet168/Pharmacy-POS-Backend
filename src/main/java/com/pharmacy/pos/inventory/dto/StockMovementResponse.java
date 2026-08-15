package com.pharmacy.pos.inventory.dto;

import com.pharmacy.pos.common.enums.MovementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementResponse {
    private Long id;
    private Long branchId;
    private String branchName;
    private Long batchId;
    private String batchNumber;
    private Long productId;
    private String productName;
    private MovementType movementType;
    private Integer quantityInBaseUnit;
    private String referenceTable;
    private Long referenceId;
    private Long performedBy;
    private LocalDateTime createdAt;
}
