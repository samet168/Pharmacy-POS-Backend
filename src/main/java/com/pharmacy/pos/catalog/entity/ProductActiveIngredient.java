package com.pharmacy.pos.catalog.entity;

import com.pharmacy.pos.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product_active_ingredients")
public class ProductActiveIngredient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "active_ingredient_id", nullable = false)
    private ActiveIngredient activeIngredient;

    private String strength;

    public Long getProductId() {
        return product != null ? product.getId() : null;
    }

    public Long getActiveIngredientId() {
        return activeIngredient != null ? activeIngredient.getId() : null;
    }
}