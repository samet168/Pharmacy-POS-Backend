package com.pharmacy.pos.inventory.mapper;

import com.pharmacy.pos.inventory.dto.BranchInventoryRequest;
import com.pharmacy.pos.inventory.dto.BranchInventoryResponse;
import com.pharmacy.pos.inventory.entity.BranchInventory;
import com.pharmacy.pos.branch.repository.BranchRepository;
import com.pharmacy.pos.inventory.repository.ProductBatchRepository;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BranchInventoryMapper {

    private final BranchRepository branchRepository;
    private final ProductBatchRepository productBatchRepository;

    public BranchInventory toEntity(BranchInventoryRequest request) {
        BranchInventory inventory = new BranchInventory();

        inventory.setBranch(branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.getBranchId())));

        inventory.setBatch(productBatchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Product batch not found with id: " + request.getBatchId())));

        inventory.setQuantityInBaseUnit(request.getQuantityInBaseUnit());
        return inventory;
    }

    public BranchInventoryResponse toResponse(BranchInventory inventory) {
        BranchInventoryResponse response = new BranchInventoryResponse();
        response.setId(inventory.getId());
        response.setBranchId(inventory.getBranch() != null ? inventory.getBranch().getId() : null);
        response.setBranchName(inventory.getBranch() != null ? inventory.getBranch().getName() : null);
        response.setBatchId(inventory.getBatch() != null ? inventory.getBatch().getId() : null);
        response.setBatchNumber(inventory.getBatch() != null ? inventory.getBatch().getBatchNumber() : null);
        response.setProductId(inventory.getBatch() != null && inventory.getBatch().getProduct() != null ? inventory.getBatch().getProduct().getId() : null);
        response.setProductName(inventory.getBatch() != null && inventory.getBatch().getProduct() != null ? inventory.getBatch().getProduct().getBrandName() : null);
        response.setQuantityInBaseUnit(inventory.getQuantityInBaseUnit());
        response.setCreatedAt(inventory.getCreatedAt());
        response.setUpdatedAt(inventory.getUpdatedAt());
        return response;
    }

    public void updateEntityFromRequest(BranchInventory inventory, BranchInventoryRequest request) {
        if (request.getBranchId() != null) {
            inventory.setBranch(branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.getBranchId())));
        }

        if (request.getBatchId() != null) {
            inventory.setBatch(productBatchRepository.findById(request.getBatchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product batch not found with id: " + request.getBatchId())));
        }

        inventory.setQuantityInBaseUnit(request.getQuantityInBaseUnit());
    }
}
