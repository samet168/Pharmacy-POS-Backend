package com.pharmacy.pos.inventory.service;

import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.inventory.dto.ProductBatchRequest;
import com.pharmacy.pos.inventory.dto.ProductBatchResponse;
import com.pharmacy.pos.inventory.entity.ProductBatch;
import com.pharmacy.pos.inventory.mapper.ProductBatchMapper;
import com.pharmacy.pos.inventory.repository.ProductBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductBatchService {

    private final ProductBatchRepository productBatchRepository;
    private final ProductBatchMapper productBatchMapper;

    public List<ProductBatchResponse> getAllBatches() {
        try {
            List<ProductBatch> batches = productBatchRepository.findAll();
            return batches.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Return empty list instead of throwing exception
            return List.of();
        }
    }

    public ProductBatchResponse getBatchById(Long id) {
        ProductBatch batch = productBatchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product batch not found with id: " + id));
        return mapToResponse(batch);
    }

    public List<ProductBatchResponse> getBatchesByProductId(Long productId) {
        try {
            List<ProductBatch> batches = productBatchRepository.findByProductId(productId);
            return batches.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<ProductBatchResponse> getBatchesByBranchId(Long branchId) {
        try {
            List<ProductBatch> batches = productBatchRepository.findByBranchId(branchId);
            return batches.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    public ProductBatchResponse createBatch(ProductBatchRequest request) {
        ProductBatch batch = productBatchMapper.toEntity(request);
        ProductBatch savedBatch = productBatchRepository.save(batch);
        return mapToResponse(savedBatch);
    }

    public ProductBatchResponse updateBatch(Long id, ProductBatchRequest request) {
        ProductBatch existingBatch = productBatchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product batch not found with id: " + id));
        
        productBatchMapper.updateEntityFromRequest(request, existingBatch);
        ProductBatch updatedBatch = productBatchRepository.save(existingBatch);
        return mapToResponse(updatedBatch);
    }

    public void deleteBatch(Long id) {
        ProductBatch batch = productBatchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product batch not found with id: " + id));
        productBatchRepository.delete(batch);
    }

    private ProductBatchResponse mapToResponse(ProductBatch batch) {
        ProductBatchResponse response = new ProductBatchResponse();
        response.setId(batch.getId());
        response.setBatchNumber(batch.getBatchNumber());
        response.setMfgDate(batch.getMfgDate());
        response.setExpiryDate(batch.getExpiryDate());
        response.setCreatedAt(batch.getCreatedAt());
        response.setUpdatedAt(batch.getUpdatedAt());
        
        // Safe null checks for relational fields
        if (batch.getProduct() != null) {
            response.setProductId(batch.getProduct().getId());
            response.setProductName(batch.getProduct().getBrandName());
            response.setProductSku(batch.getProduct().getSku());
        }
        
        // Calculate total quantity from all branch inventories
        if (batch.getBranchInventories() != null) {
            int totalQuantity = batch.getBranchInventories().stream()
                    .mapToInt(bi -> bi.getQuantityInBaseUnit())
                    .sum();
            response.setQuantityRemaining(totalQuantity);
        }
        
        return response;
    }
}