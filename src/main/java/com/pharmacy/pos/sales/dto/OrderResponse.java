package com.pharmacy.pos.sales.dto;

import com.pharmacy.pos.common.enums.OrderStatus;
import com.pharmacy.pos.common.enums.SyncStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private String clientUuid;
    private String invoiceNumber;
    private Long organizationId;
    private Long branchId;
    private Long deviceId;
    private Long userId;
    private Long customerId;
    private Long shiftId;
    private Long prescriptionId;
    private String prescriptionUrl;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal grandTotal;
    private OrderStatus status;
    private SyncStatus syncStatus;
    private LocalDateTime createdAtDevice;
    private LocalDateTime createdAt;
}
