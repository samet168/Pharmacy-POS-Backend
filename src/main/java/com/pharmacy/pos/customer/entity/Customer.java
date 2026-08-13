package com.pharmacy.pos.customer.entity;

import com.pharmacy.pos.common.TimestampEntity;
import com.pharmacy.pos.tenant.entity.Organization;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customers", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"organization_id", "phone"})
})
public class Customer extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(nullable = false)
    private String name;

    private String phone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "loyalty_points")
    private Integer loyaltyPoints = 0;

    @OneToMany(mappedBy = "customer")
    private Set<CustomerAllergy> allergies = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    private Set<Prescription> prescriptions = new HashSet<>();
}
