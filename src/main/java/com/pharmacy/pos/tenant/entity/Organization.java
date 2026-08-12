package com.pharmacy.pos.tenant.entity;

import com.pharmacy.pos.common.TimestampEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "organizations")
public class Organization extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String slug;

    private String licenseNumber;

    private String contactEmail;

    private String contactPhone;

    private String address;

    private String logoUrl;

    @Column(name = "base_currency", length = 3)
    private String baseCurrency = "USD";

    @Column(name = "is_active")
    private boolean isActive = true;

    @OneToMany(mappedBy = "organization")
    private Set<SubscriptionPlan> subscriptionPlans = new HashSet<>();
}