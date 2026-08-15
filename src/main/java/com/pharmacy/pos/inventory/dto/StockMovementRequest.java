package com.pharmacy.pos.inventory.dto;

import com.pharmacy.pos.common.enums.MovementType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockMovementRequest {
    @NotNull
    private Long branchId;

    @NotNull
    private Long batchId;

    @NotNull
    private MovementType movementType;

    @NotNull
    private Integer quantityInBaseUnit;

    private String referenceTable;

    private Long referenceId;

    private Long performedBy;
}
