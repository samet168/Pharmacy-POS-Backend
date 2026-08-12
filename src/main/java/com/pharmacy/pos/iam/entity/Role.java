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
@Table(name = "roles")
public class Role extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_system_role")
    private boolean systemRole = false;

    @OneToMany(mappedBy = "role")
    private Set<RolePermission> rolePermissions = new HashSet<>();

    @Transient
    public Set<Permission> getPermissions() {
        Set<Permission> permissions = new HashSet<>();
        for (RolePermission rp : rolePermissions) {
            permissions.add(rp.getPermission());
        }
        return permissions;
    }
}
