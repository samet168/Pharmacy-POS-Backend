package com.pharmacy.pos.customer.entity;

import com.pharmacy.pos.catalog.entity.ActiveIngredient;
import com.pharmacy.pos.common.TimestampEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customer_allergies")
public class CustomerAllergy extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", nullable = false)
    private ActiveIngredient ingredient;

    @Column(name = "reaction_notes")
    private String reactionNotes;
}
