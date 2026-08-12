package com.pharmacy.pos.sales.dto;

import com.pharmacy.pos.common.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotBlank
    private String clientUuid;

    @NotBlank
    private String invoiceNumber;

    @NotNull
    private Long organizationId;

    @NotNull
    private Long branchId;

    private Long deviceId;

    @NotNull
    private Long userId;

    private Long customerId;

    private Long shiftId;

    private Long prescriptionId;

    private String prescriptionUrl;

    @NotNull
    private BigDecimal subtotal;

    private BigDecimal discountAmount = BigDecimal.ZERO;

    private BigDecimal taxAmount = BigDecimal.ZERO;

    @NotNull
    private BigDecimal grandTotal;

    private OrderStatus status = OrderStatus.COMPLETED;
}
