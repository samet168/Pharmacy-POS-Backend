package com.pharmacy.pos.iam.service;

import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.iam.dto.RoleRequest;
import com.pharmacy.pos.iam.dto.RoleResponse;
import com.pharmacy.pos.iam.entity.Role;
import com.pharmacy.pos.iam.mapper.RoleMapper;
import com.pharmacy.pos.iam.repository.RoleRepository;
import com.pharmacy.pos.tenant.entity.Organization;
import com.pharmacy.pos.tenant.repository.OrganizationRepository;
import com.pharmacy.pos.iam.repository.RolePermissionRepository;
import com.pharmacy.pos.iam.repository.PermissionRepository;
import com.pharmacy.pos.iam.mapper.PermissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final OrganizationRepository organizationRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final com.pharmacy.pos.iam.repository.PermissionRepository permissionRepository;
    private final com.pharmacy.pos.iam.mapper.PermissionMapper permissionMapper;

    @Transactional
    public RoleResponse create(RoleRequest request) {
        Role role = roleMapper.toEntity(request);

        if (request.getOrganizationId() != null) {
            Organization organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));
            role.setOrganization(organization);
        }

        role = roleRepository.save(role);
        return roleMapper.toResponse(role);
    }

    @Transactional
    public RoleResponse update(Long id, RoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));

        if (request.getOrganizationId() != null &&
            (role.getOrganization() == null || !role.getOrganization().getId().equals(request.getOrganizationId()))) {
            Organization organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));
            role.setOrganization(organization);
        }

        roleMapper.updateEntityFromRequest(request, role);
        role = roleRepository.save(role);
        return roleMapper.toResponse(role);
    }

    public RoleResponse getById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));
        return roleMapper.toResponse(role);
    }

    public Page<RoleResponse> getAll(Pageable pageable) {
        return roleRepository.findAll(pageable)
                .map(roleMapper::toResponse);
    }

    public Page<RoleResponse> getByOrganization(Long organizationId, Pageable pageable) {
        Page<Role> roles = roleRepository.findByOrganizationId(organizationId, pageable);
        if (roles.isEmpty()) {
            // Return all roles as fallback if organization-specific roles are empty
            return roleRepository.findAll(pageable).map(roleMapper::toResponse);
        }
        return roles.map(roleMapper::toResponse);
    }

    public java.util.List<com.pharmacy.pos.iam.dto.PermissionResponse> getRolePermissions(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", roleId));
        return rolePermissionRepository.findPermissionsByRoleId(role.getId())
                .stream()
                .map(permissionMapper::toResponse)
                .toList();
    }

    @Transactional
    public java.util.List<com.pharmacy.pos.iam.dto.PermissionResponse> updateRolePermissions(Long roleId, java.util.List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", roleId));

        // Remove existing assignments quickly in batch and flush immediately
        java.util.List<com.pharmacy.pos.iam.entity.RolePermission> existing = rolePermissionRepository.findByRoleId(role.getId());
        if (!existing.isEmpty()) {
            rolePermissionRepository.deleteAllInBatch(existing);
            rolePermissionRepository.flush();
        }

        if (permissionIds != null && !permissionIds.isEmpty()) {
            java.util.List<com.pharmacy.pos.iam.entity.RolePermission> toSave = new java.util.ArrayList<>();
            for (Long permId : permissionIds) {
                com.pharmacy.pos.iam.entity.Permission permission = permissionRepository.findById(permId).orElse(null);
                if (permission != null) {
                    com.pharmacy.pos.iam.entity.RolePermission rp = new com.pharmacy.pos.iam.entity.RolePermission();
                    rp.setRole(role);
                    rp.setPermission(permission);
                    toSave.add(rp);
                }
            }
            if (!toSave.isEmpty()) {
                rolePermissionRepository.saveAll(toSave);
            }
        }

        return rolePermissionRepository.findPermissionsByRoleId(role.getId())
                .stream()
                .map(permissionMapper::toResponse)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));
        rolePermissionRepository.deleteByRoleId(role.getId());
        roleRepository.delete(role);
    }
}
