package com.pharmacy.pos.inventory.mapper;

import com.pharmacy.pos.inventory.dto.StockTransferRequest;
import com.pharmacy.pos.inventory.dto.StockTransferResponse;
import com.pharmacy.pos.inventory.entity.StockTransfer;
import com.pharmacy.pos.branch.repository.BranchRepository;
import com.pharmacy.pos.inventory.repository.ProductBatchRepository;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockTransferMapper {

    private final BranchRepository branchRepository;
    private final ProductBatchRepository productBatchRepository;

    public StockTransfer toEntity(StockTransferRequest request) {
        StockTransfer transfer = new StockTransfer();

        transfer.setFromBranch(branchRepository.findById(request.getFromBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("From branch not found with id: " + request.getFromBranchId())));

        transfer.setToBranch(branchRepository.findById(request.getToBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("To branch not found with id: " + request.getToBranchId())));

        transfer.setBatch(productBatchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Product batch not found with id: " + request.getBatchId())));

        transfer.setQuantity(request.getQuantity());
        transfer.setStatus(request.getStatus());
        transfer.setNotes(request.getNotes());
        transfer.setRequestedBy(request.getRequestedBy());
        transfer.setApprovedBy(request.getApprovedBy());
        transfer.setReceivedBy(request.getReceivedBy());
        transfer.setEstimatedArrival(request.getEstimatedArrival());
        return transfer;
    }

    public StockTransferResponse toResponse(StockTransfer transfer) {
        StockTransferResponse response = new StockTransferResponse();
        response.setId(transfer.getId());
        response.setFromBranchId(transfer.getFromBranch() != null ? transfer.getFromBranch().getId() : null);
        response.setFromBranchName(transfer.getFromBranch() != null ? transfer.getFromBranch().getName() : null);
        response.setToBranchId(transfer.getToBranch() != null ? transfer.getToBranch().getId() : null);
        response.setToBranchName(transfer.getToBranch() != null ? transfer.getToBranch().getName() : null);
        response.setBatchId(transfer.getBatch() != null ? transfer.getBatch().getId() : null);
        response.setBatchNumber(transfer.getBatch() != null ? transfer.getBatch().getBatchNumber() : null);
        response.setProductId(transfer.getBatch() != null && transfer.getBatch().getProduct() != null ? transfer.getBatch().getProduct().getId() : null);
        response.setProductName(transfer.getBatch() != null && transfer.getBatch().getProduct() != null ? transfer.getBatch().getProduct().getBrandName() : null);
        response.setQuantity(transfer.getQuantity());
        response.setStatus(transfer.getStatus());
        response.setNotes(transfer.getNotes());
        response.setRequestedBy(transfer.getRequestedBy());
        response.setApprovedBy(transfer.getApprovedBy());
        response.setApprovedAt(transfer.getApprovedAt());
        response.setReceivedBy(transfer.getReceivedBy());
        response.setReceivedAt(transfer.getReceivedAt());
        response.setEstimatedArrival(transfer.getEstimatedArrival());
        response.setCreatedAt(transfer.getCreatedAt());
        response.setUpdatedAt(transfer.getUpdatedAt());
        return response;
    }

    public void updateEntityFromRequest(StockTransfer transfer, StockTransferRequest request) {
        if (request.getFromBranchId() != null) {
            transfer.setFromBranch(branchRepository.findById(request.getFromBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("From branch not found with id: " + request.getFromBranchId())));
        }

        if (request.getToBranchId() != null) {
            transfer.setToBranch(branchRepository.findById(request.getToBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("To branch not found with id: " + request.getToBranchId())));
        }

        if (request.getBatchId() != null) {
            transfer.setBatch(productBatchRepository.findById(request.getBatchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product batch not found with id: " + request.getBatchId())));
        }

        transfer.setQuantity(request.getQuantity());
        transfer.setStatus(request.getStatus());
        transfer.setNotes(request.getNotes());
        transfer.setApprovedBy(request.getApprovedBy());
        transfer.setReceivedBy(request.getReceivedBy());
        transfer.setEstimatedArrival(request.getEstimatedArrival());
    }
}
