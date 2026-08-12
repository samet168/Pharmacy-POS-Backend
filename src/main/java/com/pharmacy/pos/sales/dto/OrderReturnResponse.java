package com.pharmacy.pos.sales.dto;

import com.pharmacy.pos.common.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderReturnResponse {

    private Long id;
    private Long orderId;
    private Long processedBy;
    private String reason;
    private BigDecimal refundAmount;
    private PaymentMethod refundMethod;
    private LocalDateTime createdAt;
}
