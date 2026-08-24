package com.pharmacy.pos.tenant.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.tenant.dto.SubscriptionCheckoutRequest;
import com.pharmacy.pos.tenant.dto.SubscriptionPlanRequest;
import com.pharmacy.pos.tenant.dto.SubscriptionPlanResponse;
import com.pharmacy.pos.tenant.service.SubscriptionPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscription-plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    @PostMapping("/checkout")
    public ApiResponse<SubscriptionPlanResponse> checkout(@Valid @RequestBody SubscriptionCheckoutRequest request) {
        return ApiResponse.success(subscriptionPlanService.checkout(request));
    }

    @PostMapping
    public ApiResponse<SubscriptionPlanResponse> create(@Valid @RequestBody SubscriptionPlanRequest request) {
        return ApiResponse.success(subscriptionPlanService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<SubscriptionPlanResponse> update(@PathVariable Long id, @Valid @RequestBody SubscriptionPlanRequest request) {
        return ApiResponse.success(subscriptionPlanService.update(id, request));
    }

    @GetMapping("/{id}")
    public ApiResponse<SubscriptionPlanResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(subscriptionPlanService.getById(id));
    }

    @GetMapping("/organization/{organizationId}")
    public ApiResponse<PageResponse<SubscriptionPlanResponse>> getByOrganization(@PathVariable Long organizationId, Pageable pageable) {
        return ApiResponse.success(PageResponse.of(subscriptionPlanService.getByOrganization(organizationId, pageable)));
    }

    @GetMapping
    public ApiResponse<PageResponse<SubscriptionPlanResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(PageResponse.of(subscriptionPlanService.getAll(pageable)));
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<SubscriptionPlanResponse> cancel(@PathVariable Long id) {
        return ApiResponse.success("Subscription cancelled successfully", subscriptionPlanService.cancel(id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        subscriptionPlanService.delete(id);
        return ApiResponse.success("Subscription plan deleted successfully", null);
    }
}
