package com.pharmacy.pos.config;
import com.pharmacy.pos.iam.entity.Permission;
import com.pharmacy.pos.iam.entity.Role;
import com.pharmacy.pos.iam.entity.RolePermission;
import com.pharmacy.pos.iam.repository.PermissionRepository;
import com.pharmacy.pos.iam.repository.RolePermissionRepository;
import com.pharmacy.pos.iam.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Initializes default permissions and assigns all permissions to all roles.
 * This ensures the application is fully functional during development
 * even when role permissions haven't been configured yet.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    /** All permission codes used by the application's @PreAuthorize annotations. */
    private static final List<String> DEFAULT_PERMISSION_CODES = List.of(
            // Organization
            "organization.view", "organization.create", "organization.update", "organization.delete",
            // Branch
            "branch.view", "branch.create", "branch.update", "branch.delete",
            // Device
            "device.view", "device.create", "device.update", "device.delete",
            // User
            "user.view", "user.create", "user.update", "user.delete",
            // Role
            "role.view", "role.create", "role.update", "role.delete",
            // Product
            "product.view", "product.create", "product.update", "product.delete",
            // Customer
            "customer.view", "customer.create", "customer.update", "customer.delete",
            // Doctor
            "doctor.view", "doctor.create", "doctor.update", "doctor.delete",
            // Order
            "order.view", "order.create", "order.update", "order.delete", "order.return",
            // Payment
            "payment.view", "payment.create", "payment.update", "payment.delete",
            // Report
            "report.view",
            // Notification
            "notification.view",
            // Shift
            "shift.view", "shift.create", "shift.update", "shift.delete"
    );

    @Override
    public void run(String... args) {
        List<Role> roles = roleRepository.findAll();
        if (roles.isEmpty()) {
            log.info("No roles found — skipping permission initialization");
            return;
        }

        // Ensure all default permissions exist in the database
        int createdPermissions = 0;
        for (String code : DEFAULT_PERMISSION_CODES) {
            if (permissionRepository.findByCode(code).isEmpty()) {
                Permission permission = new Permission();
                permission.setCode(code);
                permission.setDescription("Permission: " + code);
                permissionRepository.save(permission);
                createdPermissions++;
            }
        }
        if (createdPermissions > 0) {
            log.info("Created {} missing default permissions", createdPermissions);
        }

        List<Permission> allPermissions = permissionRepository.findAll();
        if (allPermissions.isEmpty()) {
            log.info("No permissions found — skipping permission assignment");
            return;
        }

        int totalAssignments = 0;
        for (Role role : roles) {
            for (Permission permission : allPermissions) {
                boolean exists = rolePermissionRepository.existsByRoleIdAndPermissionId(role.getId(), permission.getId());
                if (!exists) {
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setRole(role);
                    rolePermission.setPermission(permission);
                    rolePermissionRepository.save(rolePermission);
                    totalAssignments++;
                }
            }
        }

        if (totalAssignments > 0) {
            log.info("Assigned {} missing permissions across {} roles", totalAssignments, roles.size());
        } else {
            log.info("All roles already have all permissions assigned");
        }
    }
}