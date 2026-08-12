package com.pharmacy.pos.tenant.service;

import com.pharmacy.pos.common.TenantContext;
import com.pharmacy.pos.common.exception.DuplicateResourceException;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.tenant.dto.OrganizationRequest;
import com.pharmacy.pos.tenant.dto.OrganizationResponse;
import com.pharmacy.pos.tenant.entity.Organization;
import com.pharmacy.pos.tenant.mapper.OrganizationMapper;
import com.pharmacy.pos.tenant.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    @Transactional
    public OrganizationResponse create(OrganizationRequest request) {
        if (organizationRepository.existsBySlug(request.getSlug())) {
            throw new DuplicateResourceException("Organization with this slug already exists");
        }

        Organization organization = organizationMapper.toEntity(request);
        organization = organizationRepository.save(organization);
        return organizationMapper.toResponse(organization);
    }

    @Transactional
    public OrganizationResponse update(Long id, OrganizationRequest request) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", id));

        if (!organization.getSlug().equals(request.getSlug()) &&
            organizationRepository.existsBySlug(request.getSlug())) {
            throw new DuplicateResourceException("Organization with this slug already exists");
        }

        organizationMapper.updateEntityFromRequest(request, organization);
        organization = organizationRepository.save(organization);
        return organizationMapper.toResponse(organization);
    }

    public OrganizationResponse getById(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", id));
        return organizationMapper.toResponse(organization);
    }

    public OrganizationResponse getBySlug(String slug) {
        Organization organization = organizationRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with slug: " + slug));
        return organizationMapper.toResponse(organization);
    }

    public Page<OrganizationResponse> getAll(Pageable pageable) {
        return organizationRepository.findAll(pageable)
                .map(organizationMapper::toResponse);
    }

    @Transactional
    public void delete(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", id));
        organizationRepository.delete(organization);
    }
}
