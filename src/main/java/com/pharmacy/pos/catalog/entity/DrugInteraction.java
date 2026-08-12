package com.pharmacy.pos.catalog.entity;

import com.pharmacy.pos.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "drug_interactions")
public class DrugInteraction extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "ingredient_a_id")
    private ActiveIngredient ingredientA;

    @ManyToOne
    @JoinColumn(name = "ingredient_b_id")
    private ActiveIngredient ingredientB;

    private String severity;

    @Column(columnDefinition = "TEXT")
    private String description;
}
