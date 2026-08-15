package com.pharmacy.pos.inventory.mapper;

import com.pharmacy.pos.inventory.dto.StockAdjustmentRequest;
import com.pharmacy.pos.inventory.dto.StockAdjustmentResponse;
import com.pharmacy.pos.inventory.entity.StockAdjustment;
import com.pharmacy.pos.branch.repository.BranchRepository;
import com.pharmacy.pos.catalog.repository.ProductRepository;
import com.pharmacy.pos.inventory.repository.ProductBatchRepository;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockAdjustmentMapper {

    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    private final ProductBatchRepository productBatchRepository;

    public StockAdjustment toEntity(StockAdjustmentRequest request) {
        StockAdjustment adjustment = new StockAdjustment();

        adjustment.setBranch(branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.getBranchId())));

        adjustment.setProduct(productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId())));

        if (request.getBatchId() != null) {
            adjustment.setBatch(productBatchRepository.findById(request.getBatchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product batch not found with id: " + request.getBatchId())));
        }

        adjustment.setAdjustmentType(request.getAdjustmentType());
        adjustment.setQuantityBefore(request.getQuantityBefore());
        adjustment.setQuantityAfter(request.getQuantityAfter());
        adjustment.setAdjustmentQuantity(request.getAdjustmentQuantity());
        adjustment.setReason(request.getReason());
        adjustment.setNotes(request.getNotes());
        adjustment.setTotalLoss(request.getTotalLoss());
        adjustment.setApprovedBy(request.getApprovedBy());
        adjustment.setPerformedBy(request.getPerformedBy());
        return adjustment;
    }

    public StockAdjustmentResponse toResponse(StockAdjustment adjustment) {
        StockAdjustmentResponse response = new StockAdjustmentResponse();
        response.setId(adjustment.getId());
        response.setBranchId(adjustment.getBranch() != null ? adjustment.getBranch().getId() : null);
        response.setBranchName(adjustment.getBranch() != null ? adjustment.getBranch().getName() : null);
        response.setProductId(adjustment.getProduct() != null ? adjustment.getProduct().getId() : null);
        response.setProductName(adjustment.getProduct() != null ? adjustment.getProduct().getBrandName() : null);
        response.setBatchId(adjustment.getBatch() != null ? adjustment.getBatch().getId() : null);
        response.setBatchNumber(adjustment.getBatch() != null ? adjustment.getBatch().getBatchNumber() : null);
        response.setAdjustmentType(adjustment.getAdjustmentType());
        response.setQuantityBefore(adjustment.getQuantityBefore());
        response.setQuantityAfter(adjustment.getQuantityAfter());
        response.setAdjustmentQuantity(adjustment.getAdjustmentQuantity());
        response.setReason(adjustment.getReason());
        response.setNotes(adjustment.getNotes());
        response.setTotalLoss(adjustment.getTotalLoss());
        response.setApprovedBy(adjustment.getApprovedBy());
        response.setApprovedAt(adjustment.getApprovedAt());
        response.setPerformedBy(adjustment.getPerformedBy());
        response.setCreatedAt(adjustment.getCreatedAt());
        response.setUpdatedAt(adjustment.getUpdatedAt());
        return response;
    }

    public void updateEntityFromRequest(StockAdjustment adjustment, StockAdjustmentRequest request) {
        if (request.getBranchId() != null) {
            adjustment.setBranch(branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.getBranchId())));
        }

        if (request.getProductId() != null) {
            adjustment.setProduct(productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId())));
        }

        if (request.getBatchId() != null) {
            adjustment.setBatch(productBatchRepository.findById(request.getBatchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product batch not found with id: " + request.getBatchId())));
        }

        adjustment.setAdjustmentType(request.getAdjustmentType());
        adjustment.setQuantityBefore(request.getQuantityBefore());
        adjustment.setQuantityAfter(request.getQuantityAfter());
        adjustment.setAdjustmentQuantity(request.getAdjustmentQuantity());
        adjustment.setReason(request.getReason());
        adjustment.setNotes(request.getNotes());
        adjustment.setTotalLoss(request.getTotalLoss());
        adjustment.setApprovedBy(request.getApprovedBy());
        adjustment.setPerformedBy(request.getPerformedBy());
    }
}
