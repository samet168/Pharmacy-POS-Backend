package com.pharmacy.pos.catalog.entity;

import com.pharmacy.pos.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product_units")
public class ProductUnit extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "unit_name", nullable = false)
    private String unitName;

    @Column(unique = true)
    private String barcode;

    @Column(name = "conversion_factor", nullable = false)
    private int conversionFactor;

    @Column(name = "is_base_unit")
    private boolean isBaseUnit = false;

    @Column(precision = 12, scale = 2)
    private BigDecimal costPrice;

    @Column(precision = 12, scale = 2)
    private BigDecimal sellingPrice;
}
