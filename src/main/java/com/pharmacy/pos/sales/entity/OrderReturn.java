package com.pharmacy.pos.sales.entity;

import com.pharmacy.pos.common.BaseEntity;
import com.pharmacy.pos.common.enums.PaymentMethod;
import com.pharmacy.pos.iam.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_returns")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderReturn extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by")
    private User processedBy;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal refundAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod refundMethod;
}
