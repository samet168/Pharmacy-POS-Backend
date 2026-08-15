package com.pharmacy.pos.inventory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BranchInventoryRequest {
    @NotNull
    private Long branchId;

    @NotNull
    private Long batchId;

    @NotNull
    private Integer quantityInBaseUnit;
}
