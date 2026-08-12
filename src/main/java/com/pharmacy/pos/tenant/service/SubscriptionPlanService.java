package com.pharmacy.pos.tenant.service;

import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.tenant.dto.SubscriptionPlanRequest;
import com.pharmacy.pos.tenant.dto.SubscriptionPlanResponse;
import com.pharmacy.pos.tenant.entity.Organization;
import com.pharmacy.pos.tenant.entity.SubscriptionPlan;
import com.pharmacy.pos.tenant.mapper.SubscriptionPlanMapper;
import com.pharmacy.pos.tenant.repository.OrganizationRepository;
import com.pharmacy.pos.tenant.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionPlanMapper subscriptionPlanMapper;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public SubscriptionPlanResponse create(SubscriptionPlanRequest request) {
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));

        SubscriptionPlan plan = subscriptionPlanMapper.toEntity(request);
        plan.setOrganization(organization);
        plan = subscriptionPlanRepository.save(plan);
        return subscriptionPlanMapper.toResponse(plan);
    }

    @Transactional
    public SubscriptionPlanResponse update(Long id, SubscriptionPlanRequest request) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubscriptionPlan", id));

        if (!plan.getOrganization().getId().equals(request.getOrganizationId())) {
            Organization organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));
            plan.setOrganization(organization);
        }

        subscriptionPlanMapper.updateEntityFromRequest(request, plan);
        plan = subscriptionPlanRepository.save(plan);
        return subscriptionPlanMapper.toResponse(plan);
    }

    public SubscriptionPlanResponse getById(Long id) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubscriptionPlan", id));
        return subscriptionPlanMapper.toResponse(plan);
    }

    public Page<SubscriptionPlanResponse> getByOrganization(Long organizationId, Pageable pageable) {
        return subscriptionPlanRepository.findByOrganizationId(organizationId, pageable)
                .map(subscriptionPlanMapper::toResponse);
    }

    public Page<SubscriptionPlanResponse> getAll(Pageable pageable) {
        return subscriptionPlanRepository.findAll(pageable)
                .map(subscriptionPlanMapper::toResponse);
    }

    @Transactional
    public void delete(Long id) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubscriptionPlan", id));
        subscriptionPlanRepository.delete(plan);
    }
}
