package com.pharmacy.pos.purchasing.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.purchasing.dto.GoodsReceiptRequest;
import com.pharmacy.pos.purchasing.dto.GoodsReceiptResponse;
import com.pharmacy.pos.purchasing.service.GoodsReceiptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/goods-receipts")
@RequiredArgsConstructor
public class GoodsReceiptController {

    private final GoodsReceiptService goodsReceiptService;

    @PostMapping
    public ApiResponse<GoodsReceiptResponse> create(@Valid @RequestBody GoodsReceiptRequest request) {
        return ApiResponse.success(goodsReceiptService.create(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<GoodsReceiptResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(goodsReceiptService.getById(id));
    }

    @GetMapping("/purchase-order/{purchaseOrderId}")
    public ApiResponse<PageResponse<GoodsReceiptResponse>> getByPurchaseOrder(@PathVariable Long purchaseOrderId, Pageable pageable) {
        return ApiResponse.success(PageResponse.of(goodsReceiptService.getByPurchaseOrder(purchaseOrderId, pageable)));
    }

    @GetMapping("/branch/{branchId}")
    public ApiResponse<PageResponse<GoodsReceiptResponse>> getByBranch(@PathVariable Long branchId, Pageable pageable) {
        return ApiResponse.success(PageResponse.of(goodsReceiptService.getByBranch(branchId, pageable)));
    }

    @GetMapping
    public ApiResponse<PageResponse<GoodsReceiptResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(PageResponse.of(goodsReceiptService.getAll(pageable)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        goodsReceiptService.delete(id);
        return ApiResponse.success("Goods Receipt deleted successfully", null);
    }
}
