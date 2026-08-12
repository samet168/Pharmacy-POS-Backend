package com.pharmacy.pos.sales.entity;

import com.pharmacy.pos.common.BaseEntity;
import com.pharmacy.pos.common.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Payment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amountPaid;

    @Column(length = 3)
    private String currency = "USD";

    @Column(precision = 10, scale = 4)
    private BigDecimal exchangeRateUsed = BigDecimal.ONE;

    @Column
    private String transactionRef;
}
