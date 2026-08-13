package com.pharmacy.pos.customer.entity;

import com.pharmacy.pos.common.TimestampEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "doctors")
public class Doctor extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "license_number")
    private String licenseNumber;

    private String phone;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "clinic_name")
    private String clinicName;

    @OneToMany(mappedBy = "doctor")
    private Set<Prescription> prescriptions = new HashSet<>();
}
