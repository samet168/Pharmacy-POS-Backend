package com.pharmacy.pos.tenant.service;

import com.pharmacy.pos.common.enums.SubscriptionPlanStatus;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.tenant.dto.SubscriptionCheckoutRequest;
import com.pharmacy.pos.tenant.dto.SubscriptionPlanRequest;
import com.pharmacy.pos.tenant.dto.SubscriptionPlanResponse;
import com.pharmacy.pos.tenant.entity.Organization;
import com.pharmacy.pos.tenant.entity.SubscriptionPlan;
import com.pharmacy.pos.tenant.mapper.SubscriptionPlanMapper;
import com.pharmacy.pos.tenant.repository.OrganizationRepository;
import com.pharmacy.pos.tenant.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionPlanMapper subscriptionPlanMapper;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public SubscriptionPlanResponse checkout(SubscriptionCheckoutRequest request) {
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));

        log.info("Processing subscription checkout for Organization ID: {}, Plan: {}", request.getOrganizationId(), request.getPlanName());

        // 1. Process payment / mock confirmation
        // (In production, process Stripe token or handle webhook confirmation here)

        // 2. Find existing active subscription or create new subscription
        List<SubscriptionPlan> existingPlans = subscriptionPlanRepository
                .findByOrganizationId(request.getOrganizationId());

        SubscriptionPlan plan;
        if (!existingPlans.isEmpty()) {
            plan = existingPlans.get(0);
        } else {
            plan = new SubscriptionPlan();
            plan.setOrganization(organization);
        }

        // 3. Update subscription parameters & status to ACTIVE
        plan.setPlanName(request.getPlanName());
        plan.setMaxBranches(request.getMaxBranches() != null ? request.getMaxBranches() : 10);
        plan.setMaxUsers(request.getMaxUsers() != null ? request.getMaxUsers() : 50);
        plan.setStatus(SubscriptionPlanStatus.ACTIVE);
        plan.setStartsAt(LocalDate.now());

        // Calculate expiration based on billing cycle (Monthly / Yearly)
        if ("YEARLY".equalsIgnoreCase(request.getBillingCycle())) {
            plan.setEndsAt(LocalDate.now().plusYears(1));
        } else {
            plan.setEndsAt(LocalDate.now().plusMonths(1));
        }

        plan = subscriptionPlanRepository.save(plan);

        // 4. Update Organization status to Active
        organization.setActive(true);
        organizationRepository.save(organization);

        log.info("Subscription activated successfully for Organization ID: {}", organization.getId());

        return subscriptionPlanMapper.toResponse(plan);
    }

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
    public SubscriptionPlanResponse cancel(Long id) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubscriptionPlan", id));
        plan.setStatus(SubscriptionPlanStatus.CANCELLED);
        plan = subscriptionPlanRepository.save(plan);
        return subscriptionPlanMapper.toResponse(plan);
    }

    @Transactional
    public void delete(Long id) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubscriptionPlan", id));
        subscriptionPlanRepository.delete(plan);
    }
}
