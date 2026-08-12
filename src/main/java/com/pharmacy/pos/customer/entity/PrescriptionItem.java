package com.pharmacy.pos.customer.entity;

import com.pharmacy.pos.catalog.entity.Product;
import com.pharmacy.pos.common.TimestampEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "prescription_items")
public class PrescriptionItem extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "dosage_instruction")
    private String dosageInstruction;

    @Column(name = "quantity_prescribed")
    private Integer quantityPrescribed;
}
