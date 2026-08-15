package com.pharmacy.pos.purchasing.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.common.enums.PurchaseStatus;
import com.pharmacy.pos.purchasing.dto.PurchaseOrderItemRequest;
import com.pharmacy.pos.purchasing.dto.PurchaseOrderRequest;
import com.pharmacy.pos.purchasing.dto.PurchaseOrderResponse;
import com.pharmacy.pos.purchasing.service.PurchaseOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @PostMapping
    public ApiResponse<PurchaseOrderResponse> create(@Valid @RequestBody PurchaseOrderRequest request) {
        return ApiResponse.success(purchaseOrderService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<PurchaseOrderResponse> update(@PathVariable Long id, @Valid @RequestBody PurchaseOrderRequest request) {
        return ApiResponse.success(purchaseOrderService.update(id, request));
    }

    @PostMapping("/{id}/items")
    public ApiResponse<PurchaseOrderResponse> addItem(@PathVariable Long id, @Valid @RequestBody PurchaseOrderItemRequest itemRequest) {
        return ApiResponse.success(purchaseOrderService.addItem(id, itemRequest));
    }

    @PostMapping("/{id}/submit")
    public ApiResponse<PurchaseOrderResponse> submit(@PathVariable Long id) {
        return ApiResponse.success(purchaseOrderService.submit(id));
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<PurchaseOrderResponse> cancel(@PathVariable Long id) {
        return ApiResponse.success(purchaseOrderService.cancel(id));
    }

    @GetMapping("/{id}")
    public ApiResponse<PurchaseOrderResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(purchaseOrderService.getById(id));
    }

    @GetMapping("/organization/{organizationId}")
    public ApiResponse<PageResponse<PurchaseOrderResponse>> getByOrganization(@PathVariable Long organizationId, Pageable pageable) {
        return ApiResponse.success(PageResponse.of(purchaseOrderService.getByOrganization(organizationId, pageable)));
    }

    @GetMapping("/branch/{branchId}")
    public ApiResponse<PageResponse<PurchaseOrderResponse>> getByBranch(@PathVariable Long branchId, Pageable pageable) {
        return ApiResponse.success(PageResponse.of(purchaseOrderService.getByBranch(branchId, pageable)));
    }

    @GetMapping("/supplier/{supplierId}")
    public ApiResponse<PageResponse<PurchaseOrderResponse>> getBySupplier(@PathVariable Long supplierId, Pageable pageable) {
        return ApiResponse.success(PageResponse.of(purchaseOrderService.getBySupplier(supplierId, pageable)));
    }

    @GetMapping("/organization/{organizationId}/status/{status}")
    public ApiResponse<PageResponse<PurchaseOrderResponse>> getByOrganizationAndStatus(
            @PathVariable Long organizationId,
            @PathVariable PurchaseStatus status,
            Pageable pageable) {
        return ApiResponse.success(PageResponse.of(purchaseOrderService.getByOrganizationAndStatus(organizationId, status, pageable)));
    }

    @GetMapping
    public ApiResponse<PageResponse<PurchaseOrderResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(PageResponse.of(purchaseOrderService.getAll(pageable)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        purchaseOrderService.delete(id);
        return ApiResponse.success("Purchase Order deleted successfully", null);
    }
}
