package com.pharmacy.pos.purchasing.entity;

import com.pharmacy.pos.common.TimestampEntity;
import com.pharmacy.pos.catalog.entity.Product;
import com.pharmacy.pos.catalog.entity.ProductUnit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "purchase_order_items")
public class PurchaseOrderItem extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "unit_id", nullable = false)
    private ProductUnit unit;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal unitCost;

    @Column(nullable = false)
    private BigDecimal subtotal;
}
