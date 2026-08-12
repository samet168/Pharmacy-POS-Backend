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
public class PaymentResponse {

    private Long id;
    private Long orderId;
    private PaymentMethod paymentMethod;
    private BigDecimal amountPaid;
    private String currency;
    private BigDecimal exchangeRateUsed;
    private String transactionRef;
    private LocalDateTime createdAt;
}
