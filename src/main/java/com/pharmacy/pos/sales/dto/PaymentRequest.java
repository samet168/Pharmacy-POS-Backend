package com.pharmacy.pos.sales.dto;

import com.pharmacy.pos.common.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private PaymentMethod paymentMethod;

    @NotNull
    private BigDecimal amountPaid;

    private String currency = "USD";

    private BigDecimal exchangeRateUsed = BigDecimal.ONE;

    private String transactionRef;
}
