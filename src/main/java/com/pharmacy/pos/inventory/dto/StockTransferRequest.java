package com.pharmacy.pos.inventory.dto;

import com.pharmacy.pos.common.enums.TransferStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StockTransferRequest {
    @NotNull
    private Long fromBranchId;

    @NotNull
    private Long toBranchId;

    @NotNull
    private Long batchId;

    @NotNull
    private Integer quantity;

    @NotNull
    private TransferStatus status;

    private String notes;

    @NotNull
    private Long requestedBy;

    private Long approvedBy;

    private Long receivedBy;

    private LocalDate estimatedArrival;
}
