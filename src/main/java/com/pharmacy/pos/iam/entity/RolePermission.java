package com.pharmacy.pos.iam.entity;

import com.pharmacy.pos.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "role_permissions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"role_id", "permission_id"})
})
public class RolePermission extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;
}
