package com.pharmacy.pos.config;

import com.pharmacy.pos.iam.entity.Permission;
import com.pharmacy.pos.iam.entity.Role;
import com.pharmacy.pos.iam.entity.RolePermission;
import com.pharmacy.pos.iam.entity.User;
import com.pharmacy.pos.iam.repository.PermissionRepository;
import com.pharmacy.pos.iam.repository.RolePermissionRepository;
import com.pharmacy.pos.iam.repository.RoleRepository;
import com.pharmacy.pos.iam.repository.UserRepository;
import com.pharmacy.pos.tenant.entity.Organization;
import com.pharmacy.pos.tenant.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Initializes default permissions, SUPERADMIN role, and root superadmin account.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;

    /** All permission codes used by the application */
    private static final List<String> DEFAULT_PERMISSION_CODES = List.of(
            // Organization & Multi-Tenant Governance
            "organization.view", "organization.create", "organization.update", "organization.delete", "organization.manage",
            // Subscriptions & SaaS
            "subscription.view", "subscription.create", "subscription.update", "subscription.delete",
            // Branch
            "branch.view", "branch.create", "branch.update", "branch.delete", "branch.settings.view", "branch.settings.update",
            // Device
            "device.view", "device.create", "device.update", "device.delete",
            // User & Role Governance
            "user.view", "user.create", "user.update", "user.delete", "user.manage",
            "role.view", "role.create", "role.update", "role.delete", "role.manage",
            "permission.view", "permission.create", "permission.update", "permission.delete",
            // Product & Formulary
            "product.view", "product.create", "product.update", "product.delete", "product.edit_price",
            "categories.view", "categories.create", "categories.update", "categories.delete",
            "suppliers.view", "suppliers.create", "suppliers.update", "suppliers.delete",
            "active-ingredients.view", "active-ingredients.create", "active-ingredients.update", "active-ingredients.delete",
            "drug_interaction.view", "drug_interaction.create", "drug_interaction.update", "drug_interaction.delete",
            // Stock & Batches
            "inventory.view", "product_batches.view", "product_batches.create", "product_batches.update", "product_batches.delete",
            "stock_movement.view", "stock.adjust", "stock.transfer",
            // Purchase & GRN
            "purchase.view", "purchase.create", "purchase.update", "purchase.delete", "purchase.approve",
            "goods_receipt.view", "goods_receipt.create", "goods_receipt.delete",
            // Customer, Doctor & Prescriptions
            "customer.view", "customer.create", "customer.update", "customer.delete",
            "doctor.view", "doctor.create", "doctor.update", "doctor.delete",
            "prescription.view", "prescription.create", "prescription.update", "prescription.delete",
            // Orders & POS Payments
            "order.view", "order.create", "order.update", "order.delete", "order.void", "order.refund", "order.return",
            "payment.view", "payment.create", "payment.update", "payment.delete",
            "promotion.view", "promotion.create", "promotion.update", "promotion.delete",
            "loyalty.view", "loyalty.create", "loyalty.update", "loyalty.delete",
            // Reports, Notifications, Audit, Shifts
            "report.view", "notification.view", "audit.view", "audit_log.view",
            "shift.view", "shift.open", "shift.close", "shift.create", "shift.update", "shift.delete",
            "settings.manage"
    );

    @Override
    public void run(String... args) {
        // 1. Ensure SUPERADMIN role exists (The highest master role in the system)
        Role superAdminRole = roleRepository.findByName("SUPERADMIN")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("SUPERADMIN");
                    r.setSystemRole(true);
                    return roleRepository.save(r);
                });

        // 2. Ensure all default permissions exist
        int createdPermissions = 0;
        for (String code : DEFAULT_PERMISSION_CODES) {
            if (permissionRepository.findByCode(code).isEmpty()) {
                Permission permission = new Permission();
                permission.setCode(code);
                permission.setDescription("Permission authority: " + code);
                permissionRepository.save(permission);
                createdPermissions++;
            }
        }
        if (createdPermissions > 0) {
            log.info("Created {} missing default permissions", createdPermissions);
        }

        List<Permission> allPermissions = permissionRepository.findAll();

        // 3. Grant 100% full permissions to SUPERADMIN and Owner roles
        roleRepository.findByName("Owner").ifPresent(ownerRole -> {
            for (Permission p : allPermissions) {
                if (!rolePermissionRepository.existsByRoleIdAndPermissionId(ownerRole.getId(), p.getId())) {
                    RolePermission rp = new RolePermission();
                    rp.setRole(ownerRole);
                    rp.setPermission(p);
                    rolePermissionRepository.save(rp);
                }
            }
        });

        for (Permission p : allPermissions) {
            if (!rolePermissionRepository.existsByRoleIdAndPermissionId(superAdminRole.getId(), p.getId())) {
                RolePermission rp = new RolePermission();
                rp.setRole(superAdminRole);
                rp.setPermission(p);
                rolePermissionRepository.save(rp);
            }
        }

        // 4. Ensure master Root SuperAdmin account exists and has 123456 password
        Organization rootOrg = organizationRepository.findById(1L).orElseGet(() -> {
            Organization org = new Organization();
            org.setName("Pharmacy POS Global Platform");
            org.setSlug("platform-root");
            org.setBaseCurrency("USD");
            org.setActive(true);
            return organizationRepository.save(org);
        });

        User rootUser = userRepository.findByUsername("superadmin").orElseGet(() -> {
            User u = new User();
            u.setUsername("superadmin");
            return u;
        });

        rootUser.setPasswordHash(passwordEncoder.encode("123456"));
        rootUser.setName("System SuperAdmin (Root)");
        rootUser.setPhone("012888999");
        rootUser.setPinCode(passwordEncoder.encode("9999"));
        rootUser.setOrganization(rootOrg);
        rootUser.setRole(superAdminRole);
        rootUser.setActive(true);
        userRepository.save(rootUser);
        log.info("Initialized root master account: superadmin / 123456 with SUPERADMIN role");
    }
}