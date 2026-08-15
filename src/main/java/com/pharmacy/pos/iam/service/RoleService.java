package com.pharmacy.pos.iam.service;

import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.iam.dto.RoleRequest;
import com.pharmacy.pos.iam.dto.RoleResponse;
import com.pharmacy.pos.iam.entity.Role;
import com.pharmacy.pos.iam.mapper.RoleMapper;
import com.pharmacy.pos.iam.repository.RoleRepository;
import com.pharmacy.pos.tenant.entity.Organization;
import com.pharmacy.pos.tenant.repository.OrganizationRepository;
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
        return roleRepository.findByOrganizationId(organizationId, pageable)
                .map(roleMapper::toResponse);
    }

    @Transactional
    public void delete(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));
        roleRepository.delete(role);
    }
}
