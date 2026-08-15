package com.pharmacy.pos.inventory.service;

import com.pharmacy.pos.inventory.dto.BranchInventoryRequest;
import com.pharmacy.pos.inventory.dto.BranchInventoryResponse;
import com.pharmacy.pos.inventory.dto.ExpiringProductResponse;
import com.pharmacy.pos.inventory.entity.BranchInventory;
import com.pharmacy.pos.inventory.entity.ProductBatch;
import com.pharmacy.pos.inventory.mapper.BranchInventoryMapper;
import com.pharmacy.pos.inventory.repository.BranchInventoryRepository;
import com.pharmacy.pos.inventory.repository.ProductBatchRepository;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchInventoryService {

    private final BranchInventoryRepository branchInventoryRepository;
    private final BranchInventoryMapper branchInventoryMapper;
    private final ProductBatchRepository productBatchRepository;

    public List<BranchInventoryResponse> getAll() {
        return branchInventoryRepository.findAll().stream()
                .map(branchInventoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<BranchInventoryResponse> getByBranch(Long branchId) {
        return branchInventoryRepository.findByBranchId(branchId).stream()
                .map(branchInventoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<BranchInventoryResponse> getAvailableBatchesByBranchAndProduct(Long branchId, Long productId) {
        return branchInventoryRepository.findAvailableBatchesByBranchAndProduct(branchId, productId).stream()
                .map(branchInventoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    public BranchInventoryResponse getById(Long id) {
        BranchInventory inventory = branchInventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch inventory not found with id: " + id));
        return branchInventoryMapper.toResponse(inventory);
    }

    @Transactional
    public BranchInventoryResponse create(BranchInventoryRequest request) {
        BranchInventory inventory = branchInventoryMapper.toEntity(request);
        BranchInventory saved = branchInventoryRepository.save(inventory);
        return branchInventoryMapper.toResponse(saved);
    }

    @Transactional
    public BranchInventoryResponse update(Long id, BranchInventoryRequest request) {
        BranchInventory inventory = branchInventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch inventory not found with id: " + id));

        branchInventoryMapper.updateEntityFromRequest(inventory, request);
        BranchInventory updated = branchInventoryRepository.save(inventory);
        return branchInventoryMapper.toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        BranchInventory inventory = branchInventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch inventory not found with id: " + id));
        branchInventoryRepository.delete(inventory);
    }

    public List<ExpiringProductResponse> getExpiringProducts(Long branchId, int days) {
        LocalDate today = LocalDate.now();
        LocalDate expiryThreshold = today.plusDays(days);
        
        List<ProductBatch> expiringBatches = productBatchRepository.findByExpiryDateBetween(today, expiryThreshold);
        
        return expiringBatches.stream()
                .filter(batch -> branchId == null || 
                        (batch.getBranchInventories() != null && 
                         batch.getBranchInventories().stream()
                                .anyMatch(bi -> bi.getBranch().getId().equals(branchId))))
                .map(batch -> {
                    int daysUntilExpiry = (int) java.time.temporal.ChronoUnit.DAYS.between(today, batch.getExpiryDate());
                    BranchInventory inventory = branchId != null ?
                            batch.getBranchInventories().stream()
                                    .filter(bi -> bi.getBranch().getId().equals(branchId))
                                    .findFirst()
                                    .orElse(null) :
                            batch.getBranchInventories().stream().findFirst().orElse(null);
                    
                    return ExpiringProductResponse.builder()
                            .productId(batch.getProduct().getId())
                            .productName(batch.getProduct().getBrandName())
                            .sku(batch.getProduct().getSku())
                            .batchNumber(batch.getBatchNumber())
                            .quantity(inventory != null ? inventory.getQuantityInBaseUnit() : 0)
                            .expiryDate(batch.getExpiryDate())
                            .daysUntilExpiry(daysUntilExpiry)
                            .branchId(inventory != null ? inventory.getBranch().getId() : null)
                            .branchName(inventory != null ? inventory.getBranch().getName() : null)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<ExpiringProductResponse> getExpiredProducts(Long branchId) {
        LocalDate today = LocalDate.now();
        
        List<ProductBatch> expiredBatches = productBatchRepository.findByExpiryDateBefore(today);
        
        return expiredBatches.stream()
                .filter(batch -> branchId == null || 
                        (batch.getBranchInventories() != null && 
                         batch.getBranchInventories().stream()
                                .anyMatch(bi -> bi.getBranch().getId().equals(branchId))))
                .map(batch -> {
                    int daysExpired = (int) java.time.temporal.ChronoUnit.DAYS.between(batch.getExpiryDate(), today);
                    BranchInventory inventory = branchId != null ?
                            batch.getBranchInventories().stream()
                                    .filter(bi -> bi.getBranch().getId().equals(branchId))
                                    .findFirst()
                                    .orElse(null) :
                            batch.getBranchInventories().stream().findFirst().orElse(null);
                    
                    return ExpiringProductResponse.builder()
                            .productId(batch.getProduct().getId())
                            .productName(batch.getProduct().getBrandName())
                            .sku(batch.getProduct().getSku())
                            .batchNumber(batch.getBatchNumber())
                            .quantity(inventory != null ? inventory.getQuantityInBaseUnit() : 0)
                            .expiryDate(batch.getExpiryDate())
                            .daysUntilExpiry(-daysExpired) // Negative for expired
                            .branchId(inventory != null ? inventory.getBranch().getId() : null)
                            .branchName(inventory != null ? inventory.getBranch().getName() : null)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<BranchInventoryResponse> getLowStock(Long branchId) {
        List<BranchInventory> inventories = branchId != null ?
                branchInventoryRepository.findByBranchId(branchId) :
                branchInventoryRepository.findAll();
        
        return inventories.stream()
                .filter(bi -> bi.getQuantityInBaseUnit() <= bi.getBatch().getProduct().getMinStockAlert())
                .map(branchInventoryMapper::toResponse)
                .collect(Collectors.toList());
    }
}
