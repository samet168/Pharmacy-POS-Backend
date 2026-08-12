package com.pharmacy.pos.branch.entity;

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
@Table(name = "branches")
public class Branch extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private String location;

    private String phone;

    @Column(name = "is_active")
    private boolean isActive = true;

    @OneToOne(mappedBy = "branch")
    private BranchSettings branchSettings;

    @OneToMany(mappedBy = "branch")
    private Set<Device> devices = new HashSet<>();
}