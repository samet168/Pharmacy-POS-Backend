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
@Table(name = "categories")
public class Category extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "parent")
    private Set<Category> children = new HashSet<>();

    @OneToMany(mappedBy = "category")
    private Set<com.pharmacy.pos.catalog.entity.Product> products = new HashSet<>();
}
