package com.pharmacy.pos.iam.repository;

import com.pharmacy.pos.iam.entity.Permission;
import com.pharmacy.pos.iam.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    Optional<RolePermission> findByRoleIdAndPermissionId(Long roleId, Long permissionId);
    boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);
    
    @Query("SELECT p FROM Permission p JOIN RolePermission rp ON p.id = rp.permission.id WHERE rp.role.id = :roleId")
    List<Permission> findPermissionsByRoleId(Long roleId);
}
