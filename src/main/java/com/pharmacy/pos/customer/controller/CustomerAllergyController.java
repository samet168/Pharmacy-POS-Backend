package com.pharmacy.pos.customer.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.customer.dto.CustomerAllergyRequest;
import com.pharmacy.pos.customer.dto.CustomerAllergyResponse;
import com.pharmacy.pos.customer.service.CustomerAllergyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer-allergies")
@RequiredArgsConstructor
public class CustomerAllergyController {

    private final CustomerAllergyService customerAllergyService;

    @PostMapping
    public ApiResponse<CustomerAllergyResponse> create(@Valid @RequestBody CustomerAllergyRequest request) {
        return ApiResponse.success(customerAllergyService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<CustomerAllergyResponse> update(@PathVariable Long id, @Valid @RequestBody CustomerAllergyRequest request) {
        return ApiResponse.success(customerAllergyService.update(id, request));
    }

    @GetMapping("/{id}")
    public ApiResponse<CustomerAllergyResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(customerAllergyService.getById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ApiResponse<List<CustomerAllergyResponse>> getByCustomer(@PathVariable Long customerId) {
        return ApiResponse.success(customerAllergyService.getByCustomer(customerId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        customerAllergyService.delete(id);
        return ApiResponse.success("Customer allergy deleted successfully", null);
    }
}
