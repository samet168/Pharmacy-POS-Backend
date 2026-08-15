package com.pharmacy.pos.catalog.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.catalog.dto.ProductUnitRequest;
import com.pharmacy.pos.catalog.dto.ProductUnitResponse;
import com.pharmacy.pos.catalog.service.ProductUnitService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-units")
@RequiredArgsConstructor
public class ProductUnitsController {

    private final ProductUnitService productUnitService;

    @PostMapping
    @Operation(summary = "Create product unit", description = "Create a new product unit")
    public ApiResponse<ProductUnitResponse> create(@Valid @RequestBody ProductUnitRequest request) {
        return ApiResponse.success(productUnitService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product unit", description = "Update an existing product unit")
    public ApiResponse<ProductUnitResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductUnitRequest request) {
        return ApiResponse.success(productUnitService.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product unit by ID", description = "Retrieve a product unit by its ID")
    public ApiResponse<ProductUnitResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(productUnitService.getById(id));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get product units by product", description = "Retrieve all units for a specific product")
    public ApiResponse<List<ProductUnitResponse>> getByProduct(@PathVariable Long productId) {
        return ApiResponse.success(productUnitService.getByProduct(productId));
    }

    @GetMapping
    @Operation(summary = "Get all product units", description = "Retrieve all product units")
    public ApiResponse<List<ProductUnitResponse>> getAll() {
        return ApiResponse.success(productUnitService.getAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product unit", description = "Delete a product unit by its ID")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        productUnitService.delete(id);
        return ApiResponse.success("Product unit deleted successfully", null);
    }
}
