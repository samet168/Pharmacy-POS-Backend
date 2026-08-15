package com.pharmacy.pos.purchasing.entity;

import com.pharmacy.pos.common.TimestampEntity;
import com.pharmacy.pos.common.enums.PurchaseStatus;
import com.pharmacy.pos.branch.entity.Branch;
import com.pharmacy.pos.catalog.entity.Supplier;
import com.pharmacy.pos.tenant.entity.Organization;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(unique = true, nullable = false)
    private String poNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseStatus status = PurchaseStatus.DRAFT;

    @Column(name = "order_date")
    private java.time.LocalDate orderDate;

    @Column(name = "expected_delivery_date")
    private java.time.LocalDate expectedDeliveryDate;

    private BigDecimal totalAmount;

    @Column(name = "created_by")
    private Long createdBy;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PurchaseOrderItem> items = new HashSet<>();

    @OneToMany(mappedBy = "purchaseOrder")
    private Set<GoodsReceipt> goodsReceipts = new HashSet<>();
}
