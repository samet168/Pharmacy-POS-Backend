package com.pharmacy.pos.catalog.entity;

import com.pharmacy.pos.common.TimestampEntity;
import com.pharmacy.pos.tenant.entity.Organization;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "products", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"organization_id", "sku"})
})
public class Product extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private String brandName;

    @ManyToOne
    @JoinColumn(name = "generic_name_id")
    private ActiveIngredient genericNameId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "default_supplier_id")
    private Supplier defaultSupplier;

    @Column(name = "requires_prescription")
    private boolean requiresPrescription = false;

    @Column(name = "is_controlled_substance")
    private boolean isControlledSubstance = false;

    @Column(name = "min_stock_alert")
    private int minStockAlert = 10;

    @Column(name = "is_active")
    private boolean active = true;

    @OneToMany(mappedBy = "product")
    private Set<ProductUnit> productUnits = new HashSet<>();
}