package com.pharmacy.pos.customer.entity;

import com.pharmacy.pos.common.TimestampEntity;
import com.pharmacy.pos.iam.entity.User;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(name = "license_number")
    private String licenseNumber;

    private String phone;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "clinic_name")
    private String clinicName;

    private String specialty;

    private String degree;

    @Column(name = "experience_years")
    private Integer experienceYears;

    private Double rating;

    @Column(name = "reviews_count")
    private Integer reviewsCount;

    private Double fee;

    @Column(name = "available_slots", length = 500)
    private String availableSlots;

    @Column(name = "available_days", length = 500)
    private String availableDays;

    @OneToMany(mappedBy = "doctor")
    private Set<Prescription> prescriptions = new HashSet<>();
}
