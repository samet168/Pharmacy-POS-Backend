package com.pharmacy.pos.catalog.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.catalog.dto.ProductRequest;
import com.pharmacy.pos.catalog.dto.ProductResponse;
import com.pharmacy.pos.catalog.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAuthority('product.create')")
    public ApiResponse<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        return ApiResponse.success(productService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('product.update')")
    public ApiResponse<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return ApiResponse.success(productService.update(id, request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('product.view')")
    public ApiResponse<ProductResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(productService.getById(id));
    }

    @GetMapping("/organization/{organizationId}")
    @PreAuthorize("hasAuthority('product.view')")
    public ApiResponse<PageResponse<ProductResponse>> getByOrganization(@PathVariable Long organizationId, Pageable pageable) {
        return ApiResponse.success(PageResponse.of(productService.getByOrganization(organizationId, pageable)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('product.view')")
    public ApiResponse<PageResponse<ProductResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(PageResponse.of(productService.getAll(pageable)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('product.delete')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ApiResponse.success("Product deleted successfully", null);
    }
}
