package com.pharmacy.pos.inventory.service;

import com.pharmacy.pos.inventory.dto.StockAdjustmentRequest;
import com.pharmacy.pos.inventory.dto.StockAdjustmentResponse;
import com.pharmacy.pos.inventory.entity.StockAdjustment;
import com.pharmacy.pos.inventory.mapper.StockAdjustmentMapper;
import com.pharmacy.pos.inventory.repository.StockAdjustmentRepository;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockAdjustmentService {

    private final StockAdjustmentRepository stockAdjustmentRepository;
    private final StockAdjustmentMapper stockAdjustmentMapper;

    public List<StockAdjustmentResponse> getAll() {
        return stockAdjustmentRepository.findAll().stream()
                .map(stockAdjustmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<StockAdjustmentResponse> getByBranch(Long branchId) {
        return stockAdjustmentRepository.findByBranchId(branchId).stream()
                .map(stockAdjustmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<StockAdjustmentResponse> getByProduct(Long productId) {
        return stockAdjustmentRepository.findByProductId(productId).stream()
                .map(stockAdjustmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<StockAdjustmentResponse> getByReason(String reason) {
        return stockAdjustmentRepository.findByReason(reason).stream()
                .map(stockAdjustmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    public StockAdjustmentResponse getById(Long id) {
        StockAdjustment adjustment = stockAdjustmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock adjustment not found with id: " + id));
        return stockAdjustmentMapper.toResponse(adjustment);
    }

    @Transactional
    public StockAdjustmentResponse create(StockAdjustmentRequest request) {
        StockAdjustment adjustment = stockAdjustmentMapper.toEntity(request);
        StockAdjustment saved = stockAdjustmentRepository.save(adjustment);
        return stockAdjustmentMapper.toResponse(saved);
    }

    @Transactional
    public StockAdjustmentResponse update(Long id, StockAdjustmentRequest request) {
        StockAdjustment adjustment = stockAdjustmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock adjustment not found with id: " + id));

        stockAdjustmentMapper.updateEntityFromRequest(adjustment, request);
        StockAdjustment updated = stockAdjustmentRepository.save(adjustment);
        return stockAdjustmentMapper.toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        StockAdjustment adjustment = stockAdjustmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock adjustment not found with id: " + id));
        stockAdjustmentRepository.delete(adjustment);
    }
}
