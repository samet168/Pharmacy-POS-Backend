package com.pharmacy.pos.customer.entity;

import com.pharmacy.pos.common.TimestampEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "prescriptions")
public class Prescription extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(name = "prescription_image_url")
    private String prescriptionImageUrl;

    @Column(name = "issued_date")
    private LocalDate issuedDate;

    @Column(name = "is_refillable")
    private boolean refillable = false;

    @Column(name = "refills_remaining")
    private Integer refillsRemaining = 0;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PrescriptionItem> items = new HashSet<>();
}
