package com.pharmacy.pos.inventory.mapper;

import com.pharmacy.pos.inventory.dto.StockMovementRequest;
import com.pharmacy.pos.inventory.dto.StockMovementResponse;
import com.pharmacy.pos.inventory.entity.StockMovement;
import com.pharmacy.pos.branch.repository.BranchRepository;
import com.pharmacy.pos.inventory.repository.ProductBatchRepository;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockMovementMapper {

    private final BranchRepository branchRepository;
    private final ProductBatchRepository productBatchRepository;

    public StockMovement toEntity(StockMovementRequest request) {
        StockMovement movement = new StockMovement();

        movement.setBranch(branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.getBranchId())));

        movement.setBatch(productBatchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Product batch not found with id: " + request.getBatchId())));

        movement.setMovementType(request.getMovementType());
        movement.setQuantityInBaseUnit(request.getQuantityInBaseUnit());
        movement.setReferenceTable(request.getReferenceTable());
        movement.setReferenceId(request.getReferenceId());
        movement.setPerformedBy(request.getPerformedBy());
        return movement;
    }

    public StockMovementResponse toResponse(StockMovement movement) {
        StockMovementResponse response = new StockMovementResponse();
        response.setId(movement.getId());
        response.setBranchId(movement.getBranch() != null ? movement.getBranch().getId() : null);
        response.setBranchName(movement.getBranch() != null ? movement.getBranch().getName() : null);
        response.setBatchId(movement.getBatch() != null ? movement.getBatch().getId() : null);
        response.setBatchNumber(movement.getBatch() != null ? movement.getBatch().getBatchNumber() : null);
        response.setProductId(movement.getBatch() != null && movement.getBatch().getProduct() != null ? movement.getBatch().getProduct().getId() : null);
        response.setProductName(movement.getBatch() != null && movement.getBatch().getProduct() != null ? movement.getBatch().getProduct().getBrandName() : null);
        response.setMovementType(movement.getMovementType());
        response.setQuantityInBaseUnit(movement.getQuantityInBaseUnit());
        response.setReferenceTable(movement.getReferenceTable());
        response.setReferenceId(movement.getReferenceId());
        response.setPerformedBy(movement.getPerformedBy());
        response.setCreatedAt(movement.getCreatedAt());
        return response;
    }

    public void updateEntityFromRequest(StockMovement movement, StockMovementRequest request) {
        if (request.getBranchId() != null) {
            movement.setBranch(branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.getBranchId())));
        }

        if (request.getBatchId() != null) {
            movement.setBatch(productBatchRepository.findById(request.getBatchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product batch not found with id: " + request.getBatchId())));
        }

        movement.setMovementType(request.getMovementType());
        movement.setQuantityInBaseUnit(request.getQuantityInBaseUnit());
        movement.setReferenceTable(request.getReferenceTable());
        movement.setReferenceId(request.getReferenceId());
        movement.setPerformedBy(request.getPerformedBy());
    }
}
