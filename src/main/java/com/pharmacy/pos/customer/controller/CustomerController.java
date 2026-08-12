package com.pharmacy.pos.customer.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.customer.dto.CustomerRequest;
import com.pharmacy.pos.customer.dto.CustomerResponse;
import com.pharmacy.pos.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @PreAuthorize("hasAuthority('customer.create')")
    public ApiResponse<CustomerResponse> create(@Valid @RequestBody CustomerRequest request) {
        return ApiResponse.success(customerService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('customer.update')")
    public ApiResponse<CustomerResponse> update(@PathVariable Long id, @Valid @RequestBody CustomerRequest request) {
        return ApiResponse.success(customerService.update(id, request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('customer.view')")
    public ApiResponse<CustomerResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(customerService.getById(id));
    }

    @GetMapping("/organization/{organizationId}/phone/{phone}")
    @PreAuthorize("hasAuthority('customer.view')")
    public ApiResponse<CustomerResponse> getByOrganizationAndPhone(
            @PathVariable Long organizationId,
            @PathVariable String phone) {
        return ApiResponse.success(customerService.getByOrganizationAndPhone(organizationId, phone));
    }

    @GetMapping("/organization/{organizationId}")
    @PreAuthorize("hasAuthority('customer.view')")
    public ApiResponse<PageResponse<CustomerResponse>> getByOrganization(@PathVariable Long organizationId, Pageable pageable) {
        return ApiResponse.success(PageResponse.of(customerService.getByOrganization(organizationId, pageable)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('customer.view')")
    public ApiResponse<PageResponse<CustomerResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(PageResponse.of(customerService.getAll(pageable)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('customer.delete')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ApiResponse.success("Customer deleted successfully", null);
    }
}
