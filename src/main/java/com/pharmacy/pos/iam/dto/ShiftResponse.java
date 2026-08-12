package com.pharmacy.pos.iam.dto;

import com.pharmacy.pos.common.enums.ShiftStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftResponse {
    private Long id;
    private Long userId;
    private Long branchId;
    private Long deviceId;
    private BigDecimal openingCash;
    private BigDecimal expectedCash;
    private BigDecimal actualCash;
    private BigDecimal difference;
    private ShiftStatus status;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
