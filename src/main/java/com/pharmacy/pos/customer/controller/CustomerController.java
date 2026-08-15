package com.pharmacy.pos.customer.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.customer.dto.CustomerRequest;
import com.pharmacy.pos.customer.dto.CustomerResponse;
import com.pharmacy.pos.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('customer.create')")
    @Operation(summary = "Create customer", description = "Create a new customer with optional image upload")
    public ApiResponse<CustomerResponse> create(
            @Parameter(description = "Customer data as JSON", required = true, content = @Content(schema = @Schema(implementation = CustomerRequest.class))) @RequestPart(value = "customer", required = true) @Valid CustomerRequest request,
            @Parameter(description = "Image file (optional)") @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
        if (file != null && !file.isEmpty()) {
            return ApiResponse.success(customerService.createWithImage(request, file));
        }
        return ApiResponse.success(customerService.create(request));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('customer.update')")
    @Operation(summary = "Update customer", description = "Update customer with optional image upload")
    public ApiResponse<CustomerResponse> update(
            @PathVariable Long id,
            @Parameter(description = "Customer data as JSON", required = true, content = @Content(schema = @Schema(implementation = CustomerRequest.class))) @RequestPart(value = "customer", required = true) @Valid CustomerRequest request,
            @Parameter(description = "Image file (optional)") @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
        if (file != null && !file.isEmpty()) {
            return ApiResponse.success(customerService.updateWithImage(id, request, file));
        }
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

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('customer.view')")
    @Operation(summary = "Search customers", description = "Search customers by name, phone, or email")
    public ApiResponse<PageResponse<CustomerResponse>> search(
            @Parameter(description = "Organization ID") @RequestParam Long organizationId,
            @Parameter(description = "Search query") @RequestParam String query,
            Pageable pageable) {
        return ApiResponse.success(PageResponse.of(customerService.search(organizationId, query, pageable)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('customer.delete')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ApiResponse.success("Customer deleted successfully", null);
    }
}
