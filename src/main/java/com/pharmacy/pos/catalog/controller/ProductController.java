package com.pharmacy.pos.catalog.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.catalog.dto.ProductRequest;
import com.pharmacy.pos.catalog.dto.ProductResponse;
import com.pharmacy.pos.catalog.service.ProductService;
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
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('product.create')")
    @Operation(summary = "Create product", description = "Create a new product with optional image upload")
    public ApiResponse<ProductResponse> create(
            @Parameter(description = "Product data as JSON", required = true, content = @Content(schema = @Schema(implementation = ProductRequest.class))) @RequestPart(value = "product", required = true) @Valid ProductRequest request,
            @Parameter(description = "Image file (optional)") @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
        if (file != null && !file.isEmpty()) {
            return ApiResponse.success(productService.createWithImage(request, file));
        }
        return ApiResponse.success(productService.create(request));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('product.update')")
    @Operation(summary = "Update product", description = "Update product with optional image upload")
    public ApiResponse<ProductResponse> update(
            @PathVariable Long id,
            @Parameter(description = "Product data as JSON", required = true, content = @Content(schema = @Schema(implementation = ProductRequest.class))) @RequestPart(value = "product", required = true) @Valid ProductRequest request,
            @Parameter(description = "Image file (optional)") @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
        if (file != null && !file.isEmpty()) {
            return ApiResponse.success(productService.updateWithImage(id, request, file));
        }
        return ApiResponse.success(productService.update(id, request));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(productService.getById(id));
    }

    @GetMapping("/organization/{organizationId}")
    public ApiResponse<PageResponse<ProductResponse>> getByOrganization(@PathVariable Long organizationId, Pageable pageable) {
        return ApiResponse.success(PageResponse.of(productService.getByOrganization(organizationId, pageable)));
    }

    @GetMapping
    public ApiResponse<PageResponse<ProductResponse>> getAll(
            @RequestParam(required = false) Long organizationId,
            org.springframework.security.core.Authentication authentication,
            Pageable pageable) {
        Long orgId = organizationId;
        if (orgId == null && authentication != null && authentication.getPrincipal() instanceof com.pharmacy.pos.security.CustomUserDetails userDetails) {
            if (userDetails.getUser().getOrganization() != null) {
                orgId = userDetails.getUser().getOrganization().getId();
            }
        }
        if (orgId != null) {
            return ApiResponse.success(PageResponse.of(productService.getByOrganization(orgId, pageable)));
        }
        return ApiResponse.success(PageResponse.of(productService.getAll(pageable)));
    }

    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Search products by name, SKU, or barcode")
    public ApiResponse<PageResponse<ProductResponse>> search(
            @Parameter(description = "Organization ID") @RequestParam(required = false) Long organizationId,
            @Parameter(description = "Search query") @RequestParam String query,
            @Parameter(description = "Branch ID (optional)") @RequestParam(required = false) Long branchId,
            Pageable pageable) {
        if (organizationId == null) {
            return ApiResponse.success(PageResponse.of(productService.searchAll(query, pageable)));
        }
        return ApiResponse.success(PageResponse.of(productService.search(organizationId, query, branchId, pageable)));
    }

    @GetMapping("/barcode/{barcode}")
    @Operation(summary = "Get product by barcode", description = "Get product by barcode/SKU")
    public ApiResponse<ProductResponse> getByBarcode(
            @Parameter(description = "Organization ID") @RequestParam(required = false) Long organizationId,
            @Parameter(description = "Barcode/SKU") @PathVariable String barcode) {
        return ApiResponse.success(productService.getByBarcode(organizationId != null ? organizationId : 1L, barcode));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('product.delete')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ApiResponse.success("Product deleted successfully", null);
    }
}
