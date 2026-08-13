package com.pharmacy.pos.setup.service;

import com.pharmacy.pos.branch.entity.Branch;
import com.pharmacy.pos.branch.entity.BranchSettings;
import com.pharmacy.pos.branch.repository.BranchRepository;
import com.pharmacy.pos.branch.repository.BranchSettingsRepository;
import com.pharmacy.pos.common.exception.BusinessRuleException;
import com.pharmacy.pos.common.exception.DuplicateResourceException;
import com.pharmacy.pos.iam.dto.LoginResponse;
import com.pharmacy.pos.iam.entity.Permission;
import com.pharmacy.pos.iam.entity.Role;
import com.pharmacy.pos.iam.entity.RolePermission;
import com.pharmacy.pos.iam.entity.User;
import com.pharmacy.pos.iam.entity.UserBranch;
import com.pharmacy.pos.iam.repository.PermissionRepository;
import com.pharmacy.pos.iam.repository.RolePermissionRepository;
import com.pharmacy.pos.iam.repository.RoleRepository;
import com.pharmacy.pos.iam.repository.UserBranchRepository;
import com.pharmacy.pos.iam.repository.UserRepository;
import com.pharmacy.pos.security.JwtService;
import com.pharmacy.pos.setup.dto.BootstrapRequest;
import com.pharmacy.pos.tenant.entity.Organization;
import com.pharmacy.pos.tenant.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SetupService {

    private final com.pharmacy.pos.tenant.repository.OrganizationRepository organizationRepository;
    private final BranchRepository branchRepository;
    private final BranchSettingsRepository branchSettingsRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserBranchRepository userBranchRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final com.pharmacy.pos.iam.repository.PermissionRepository permissionRepository;
    private final com.pharmacy.pos.iam.repository.RolePermissionRepository rolePermissionRepository;

    @Transactional
    public LoginResponse bootstrap(BootstrapRequest request) {
        // Check if any organization already exists - return 409 if already initialized
        if (organizationRepository.count() > 0) {
            throw new DuplicateResourceException("System already initialized. Bootstrap can only be run once on a fresh database.");
        }

        // 1. Create Organization
        com.pharmacy.pos.tenant.entity.Organization organization = new com.pharmacy.pos.tenant.entity.Organization();
        organization.setName(request.getOrganizationName());
        organization.setSlug(request.getOrganizationSlug());
        organization.setBaseCurrency("USD");
        organization.setActive(true);
        organization = organizationRepository.save(organization);

        // 2. Create Branch
        Branch branch = new Branch();
        branch.setOrganization(organization);
        branch.setCode(request.getBranchCode());
        branch.setName(request.getBranchName());
        branch.setActive(true);
        branch = branchRepository.save(branch);

        // 3. Create Branch Settings
        BranchSettings branchSettings = new BranchSettings();
        branchSettings.setBranch(branch);
        branchSettings.setTaxRate(java.math.BigDecimal.ZERO);
        branchSettings.setAllowNegativeStock(false);
        branchSettingsRepository.save(branchSettings);

        // 4. Create System Roles
        Role ownerRole = createSystemRole("Owner", true);
        Role managerRole = createSystemRole("Manager", true);
        Role pharmacistRole = createSystemRole("Pharmacist", true);
        Role cashierRole = createSystemRole("Cashier", true);

        // 5. Create Permissions and assign to all roles as per requirements
        createPermissionsAndAssignToRoles(ownerRole, managerRole, pharmacistRole, cashierRole);

        // 6. Create User (Admin/Owner)
        User user = new User();
        user.setUsername(request.getAdminUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getAdminPassword()));
        user.setName(request.getAdminName());
        user.setPinCode(request.getAdminPinCode());
        user.setOrganization(organization);
        user.setRole(ownerRole);
        user.setActive(true);
        user = userRepository.save(user);

        // 7. Create User-Branch association
        UserBranch userBranch = new UserBranch();
        userBranch.setUser(user);
        userBranch.setBranch(branch);
        userBranchRepository.save(userBranch);

        // 8. Generate JWT tokens
        List<Long> branchIds = List.of(branch.getId());

        String accessToken = jwtService.generateAccessToken(
                user.getId(),
                user.getOrganization().getId(),
                user.getRole().getId(),
                branchIds
        );

        String refreshToken = jwtService.generateRefreshToken(user.getId());

        log.info("Bootstrap completed successfully for organization: {}", organization.getSlug());

        return new LoginResponse(
                accessToken,
                refreshToken,
                user.getId(),
                user.getUsername(),
                user.getOrganization().getId(),
                user.getRole().getId(),
                user.getRole().getName()
        );
    }

    private Role createSystemRole(String name, boolean isSystemRole) {
        Role role = new Role();
        role.setName(name);
        role.setSystemRole(isSystemRole);
        return roleRepository.save(role);
    }

    private void createPermissionsAndAssignToRoles(Role ownerRole, Role managerRole, Role pharmacistRole, Role cashierRole) {
        // Create all permissions that match controller requirements
        String[] permissionCodes = {
            "order.void", "order.refund", "order.create", "order.view", "order.update", "order.delete", "order.return",
            "product.edit_price", "product.create", "product.view", "product.update", "product.delete",
            "stock.adjust", "stock.transfer",
            "purchase.create", "purchase.approve", "purchase.view", "purchase.update", "purchase.submit", "purchase.cancel", "purchase.delete", "purchase.receive",
            "payment.create", "payment.view", "payment.update", "payment.delete",
            "shift.open", "shift.close", "shift.reconcile", "shift.view", "shift.delete",
            "user.view", "user.create", "user.update", "user.delete", "user.manage",
            "role.view", "role.create", "role.update", "role.delete", "role.manage",
            "permission.view", "permission.create", "permission.update", "permission.delete",
            "branch.view", "branch.create", "branch.update", "branch.delete", "branch.manage", "branch.settings.view", "branch.settings.update",
            "organization.view", "organization.create", "organization.update", "organization.delete",
            "subscription.view", "subscription.create", "subscription.update", "subscription.delete",
            "settings.manage", "report.view", "audit.view",
            "customer.view", "customer.create", "customer.update", "customer.delete", "customer.manage",
            "doctor.view", "doctor.create", "doctor.update", "doctor.delete", "doctor.manage",
            "prescription.view", "prescription.create", "prescription.update", "prescription.delete", "prescription.manage",
            "device.view", "device.create", "device.update", "device.delete",
            "goods_receipt.view", "goods_receipt.create", "goods_receipt.delete"
        };

        for (String code : permissionCodes) {
            Permission permission = permissionRepository.findByCode(code)
                    .orElseGet(() -> {
                        Permission newPermission = new Permission();
                        newPermission.setCode(code);
                        newPermission.setDescription("Permission for " + code);
                        return permissionRepository.save(newPermission);
                    });

            // Owner: ALL permissions
            if (!rolePermissionRepository.existsByRoleIdAndPermissionId(ownerRole.getId(), permission.getId())) {
                assignPermissionToRole(ownerRole, permission);
                counters[1]++;
            }

            // Manager: all except user.manage, role.manage, permission.manage, branch.manage, settings.manage, organization.create/update/delete, subscription.create/update/delete
            if (!code.equals("user.manage") && !code.equals("role.manage") && !code.equals("permission.manage") &&
                !code.equals("branch.manage") && !code.equals("settings.manage") &&
                !code.equals("organization.create") && !code.equals("organization.update") && !code.equals("organization.delete") &&
                !code.equals("subscription.create") && !code.equals("subscription.update") && !code.equals("subscription.delete")) {
                assignPermissionToRole(managerRole, permission);
            }

            // Pharmacist: order.void, stock.adjust, shift.open, shift.close, report.view, customer.view, doctor.view, prescription.view
            if (code.equals("order.void") || code.equals("stock.adjust") || 
                code.equals("shift.open") || code.equals("shift.close") || code.equals("report.view") ||
                code.equals("customer.view") || code.equals("doctor.view") || code.equals("prescription.view")) {
                assignPermissionToRole(pharmacistRole, permission);
            }

            // Cashier: shift.open, shift.close
            if (code.equals("shift.open") || code.equals("shift.close")) {
                assignPermissionToRole(cashierRole, permission);
            }
        }
    }

    private void assignPermissionToRole(Role role, Permission permission) {
        if (!rolePermissionRepository.existsByRoleIdAndPermissionId(role.getId(), permission.getId())) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRole(role);
            rolePermission.setPermission(permission);
            rolePermissionRepository.save(rolePermission);
        }
    }

    @Transactional
    public String fixPermissions() {
        // Find all system roles
        Role ownerRole = roleRepository.findByNameAndSystemRole("Owner", true)
                .orElseThrow(() -> new BusinessRuleException("Owner role not found"));
        Role managerRole = roleRepository.findByNameAndSystemRole("Manager", true)
                .orElseThrow(() -> new BusinessRuleException("Manager role not found"));
        Role pharmacistRole = roleRepository.findByNameAndSystemRole("Pharmacist", true)
                .orElseThrow(() -> new BusinessRuleException("Pharmacist role not found"));
        Role cashierRole = roleRepository.findByNameAndSystemRole("Cashier", true)
                .orElseThrow(() -> new BusinessRuleException("Cashier role not found"));

        // Define ALL permissions that should exist
        String[] permissionCodes = {
            "order.void", "order.refund", "order.create", "order.view", "order.update", "order.delete", "order.return",
            "product.edit_price", "product.create", "product.view", "product.update", "product.delete",
            "stock.adjust", "stock.transfer",
            "purchase.create", "purchase.approve", "purchase.view", "purchase.update", "purchase.submit", "purchase.cancel", "purchase.delete", "purchase.receive",
            "payment.create", "payment.view", "payment.update", "payment.delete",
            "shift.open", "shift.close", "shift.reconcile", "shift.view", "shift.delete",
            "user.view", "user.create", "user.update", "user.delete", "user.manage",
            "role.view", "role.create", "role.update", "role.delete", "role.manage",
            "permission.view", "permission.create", "permission.update", "permission.delete",
            "branch.view", "branch.create", "branch.update", "branch.delete", "branch.manage", "branch.settings.view", "branch.settings.update",
            "organization.view", "organization.create", "organization.update", "organization.delete",
            "subscription.view", "subscription.create", "subscription.update", "subscription.delete",
            "settings.manage", "report.view", "audit.view",
            "customer.view", "customer.create", "customer.update", "customer.delete", "customer.manage",
            "doctor.view", "doctor.create", "doctor.update", "doctor.delete", "doctor.manage",
            "prescription.view", "prescription.create", "prescription.update", "prescription.delete", "prescription.manage",
            "device.view", "device.create", "device.update", "device.delete",
            "goods_receipt.view", "goods_receipt.create", "goods_receipt.delete"
        };

        final int[] counters = {0, 0}; // [createdCount, assignedCount]

        for (String code : permissionCodes) {
            // Get or create permission
            Permission permission = permissionRepository.findByCode(code)
                    .orElseGet(() -> {
                        Permission newPermission = new Permission();
                        newPermission.setCode(code);
                        newPermission.setDescription("Permission for " + code);
                        counters[0]++;
                        return permissionRepository.save(newPermission);
                    });

            // Owner: ALL permissions
            if (!rolePermissionRepository.existsByRoleIdAndPermissionId(ownerRole.getId(), permission.getId())) {
                assignPermissionToRole(ownerRole, permission);
                counters[1]++;
            }

            // Manager: all except user.manage, role.manage, branch.manage, settings.manage
            if (!code.equals("user.manage") && !code.equals("role.manage") && 
                !code.equals("branch.manage") && !code.equals("settings.manage")) {
                if (!rolePermissionRepository.existsByRoleIdAndPermissionId(managerRole.getId(), permission.getId())) {
                    assignPermissionToRole(managerRole, permission);
                    counters[1]++;
                }
            }

            // Pharmacist: order.void, stock.adjust, shift.open, shift.close, report.view, customer.view, doctor.view, prescription.view
            if (code.equals("order.void") || code.equals("stock.adjust") || 
                code.equals("shift.open") || code.equals("shift.close") || code.equals("report.view") ||
                code.equals("customer.view") || code.equals("doctor.view") || code.equals("prescription.view")) {
                if (!rolePermissionRepository.existsByRoleIdAndPermissionId(pharmacistRole.getId(), permission.getId())) {
                    assignPermissionToRole(pharmacistRole, permission);
                    counters[1]++;
                }
            }

            // Cashier: shift.open, shift.close
            if (code.equals("shift.open") || code.equals("shift.close")) {
                if (!rolePermissionRepository.existsByRoleIdAndPermissionId(cashierRole.getId(), permission.getId())) {
                    assignPermissionToRole(cashierRole, permission);
                    counters[1]++;
                }
            }
        }

        return String.format("Permissions fixed successfully for all roles. Created %d new permissions, assigned %d permissions to roles. Total: %d permissions.", 
            counters[0], counters[1], permissionCodes.length);
    }
}