package com.pharmacy.pos.inventory.service;

import com.pharmacy.pos.inventory.dto.StockMovementRequest;
import com.pharmacy.pos.inventory.dto.StockMovementResponse;
import com.pharmacy.pos.inventory.entity.StockMovement;
import com.pharmacy.pos.inventory.mapper.StockMovementMapper;
import com.pharmacy.pos.inventory.repository.StockMovementRepository;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final StockMovementMapper stockMovementMapper;

    public List<StockMovementResponse> getAll() {
        return stockMovementRepository.findAll().stream()
                .map(stockMovementMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<StockMovementResponse> getByBranch(Long branchId) {
        return stockMovementRepository.findByBranchId(branchId).stream()
                .map(stockMovementMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<StockMovementResponse> getByBatch(Long batchId) {
        return stockMovementRepository.findByBatchId(batchId).stream()
                .map(stockMovementMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<StockMovementResponse> getByReference(String referenceTable, Long referenceId) {
        return stockMovementRepository.findByReferenceTableAndReferenceId(referenceTable, referenceId).stream()
                .map(stockMovementMapper::toResponse)
                .collect(Collectors.toList());
    }

    public StockMovementResponse getById(Long id) {
        StockMovement movement = stockMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock movement not found with id: " + id));
        return stockMovementMapper.toResponse(movement);
    }

    @Transactional
    public StockMovementResponse create(StockMovementRequest request) {
        StockMovement movement = stockMovementMapper.toEntity(request);
        StockMovement saved = stockMovementRepository.save(movement);
        return stockMovementMapper.toResponse(saved);
    }

    @Transactional
    public StockMovementResponse update(Long id, StockMovementRequest request) {
        StockMovement movement = stockMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock movement not found with id: " + id));

        stockMovementMapper.updateEntityFromRequest(movement, request);
        StockMovement updated = stockMovementRepository.save(movement);
        return stockMovementMapper.toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        StockMovement movement = stockMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock movement not found with id: " + id));
        stockMovementRepository.delete(movement);
    }
}
