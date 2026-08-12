package com.pharmacy.pos.branch.service;

import com.pharmacy.pos.common.TenantContext;
import com.pharmacy.pos.common.exception.DuplicateResourceException;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.branch.dto.BranchRequest;
import com.pharmacy.pos.branch.dto.BranchResponse;
import com.pharmacy.pos.branch.entity.Branch;
import com.pharmacy.pos.branch.mapper.BranchMapper;
import com.pharmacy.pos.branch.repository.BranchRepository;
import com.pharmacy.pos.tenant.entity.Organization;
import com.pharmacy.pos.tenant.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public BranchResponse create(BranchRequest request) {
        if (branchRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Branch with this code already exists");
        }

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));

        Branch branch = branchMapper.toEntity(request);
        branch.setOrganization(organization);
        branch = branchRepository.save(branch);
        return branchMapper.toResponse(branch);
    }

    @Transactional
    public BranchResponse update(Long id, BranchRequest request) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", id));

        if (!branch.getCode().equals(request.getCode()) &&
            branchRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Branch with this code already exists");
        }

        if (!branch.getOrganization().getId().equals(request.getOrganizationId())) {
            Organization organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));
            branch.setOrganization(organization);
        }

        branchMapper.updateEntityFromRequest(request, branch);
        branch = branchRepository.save(branch);
        return branchMapper.toResponse(branch);
    }

    public BranchResponse getById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", id));
        return branchMapper.toResponse(branch);
    }

    public Page<BranchResponse> getByOrganization(Long organizationId, Pageable pageable) {
        return branchRepository.findByOrganizationId(organizationId, pageable)
                .map(branchMapper::toResponse);
    }

    public Page<BranchResponse> getAll(Pageable pageable) {
        return branchRepository.findAll(pageable)
                .map(branchMapper::toResponse);
    }

    @Transactional
    public void delete(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", id));
        branchRepository.delete(branch);
    }
}
