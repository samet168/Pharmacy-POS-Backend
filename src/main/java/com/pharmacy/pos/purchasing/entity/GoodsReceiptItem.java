package com.pharmacy.pos.purchasing.entity;

import com.pharmacy.pos.common.TimestampEntity;
import com.pharmacy.pos.catalog.entity.Product;
import com.pharmacy.pos.catalog.entity.ProductUnit;
import com.pharmacy.pos.inventory.entity.ProductBatch;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "goods_receipt_items")
public class GoodsReceiptItem extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "goods_receipt_id", nullable = false)
    private GoodsReceipt goodsReceipt;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private ProductBatch batch;

    @ManyToOne
    @JoinColumn(name = "unit_id", nullable = false)
    private ProductUnit unit;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal unitCost;
}
