package com.pharmacy.pos.iam.entity;

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
@Table(name = "users")
public class User extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    private String phone;

    private String pinCode;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_active")
    private boolean active = true;

    @OneToMany(mappedBy = "user")
    private Set<UserBranch> userBranches = new HashSet<>();
}