package com.pharmacy.pos.branch.entity;

import com.pharmacy.pos.common.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "branch_settings", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"branch_id"})
})
public class BranchSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "branch_id", nullable = false, unique = true)
    private Branch branch;

    @Column(precision = 5, scale = 2)
    private BigDecimal taxRate = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String receiptHeader;

    @Column(columnDefinition = "TEXT")
    private String receiptFooter;

    @Column(name = "allow_negative_stock")
    private boolean allowNegativeStock = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_payment_method")
    private PaymentMethod defaultPaymentMethod;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
