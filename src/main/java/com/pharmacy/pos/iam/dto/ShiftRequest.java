package com.pharmacy.pos.iam.dto;

import com.pharmacy.pos.common.enums.ShiftStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShiftRequest {
    @NotNull
    private Long userId;

    @NotNull
    private Long branchId;

    private Long deviceId;

    @NotNull
    private BigDecimal openingCash;

    private BigDecimal expectedCash;

    private BigDecimal actualCash;

    private ShiftStatus status;
}
