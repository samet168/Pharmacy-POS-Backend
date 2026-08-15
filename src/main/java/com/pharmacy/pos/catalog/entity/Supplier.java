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
@Table(name = "suppliers")
public class Supplier extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(nullable = false)
    private String name;

    @Column(name = "tax_id")
    private String taxId;

    private String contactPerson;

    private String phone;

    private String email;

    private String address;

    @Column(name = "is_active")
    private boolean isActive = true;

    @OneToMany(mappedBy = "defaultSupplier")
    private Set<Product> products = new HashSet<>();
}
