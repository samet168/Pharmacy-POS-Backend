package com.pharmacy.pos.purchasing.entity;

import com.pharmacy.pos.common.TimestampEntity;
import com.pharmacy.pos.branch.entity.Branch;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "goods_receipts")
public class GoodsReceipt extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "received_by")
    private Long receivedBy;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    private String notes;

    @OneToMany(mappedBy = "goodsReceipt", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GoodsReceiptItem> items = new HashSet<>();
}
