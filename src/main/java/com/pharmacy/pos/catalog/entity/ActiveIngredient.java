package com.pharmacy.pos.catalog.entity;

import com.pharmacy.pos.common.TimestampEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "active_ingredients")
public class ActiveIngredient extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "ingredientA")
    private Set<DrugInteraction> interactionsAsIngredientA = new HashSet<>();

    @OneToMany(mappedBy = "ingredientB")
    private Set<DrugInteraction> interactionsAsIngredientB = new HashSet<>();
}
