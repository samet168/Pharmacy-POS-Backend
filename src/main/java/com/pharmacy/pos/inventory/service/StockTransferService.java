package com.pharmacy.pos.inventory.service;

import com.pharmacy.pos.inventory.dto.StockTransferRequest;
import com.pharmacy.pos.inventory.dto.StockTransferResponse;
import com.pharmacy.pos.inventory.entity.StockTransfer;
import com.pharmacy.pos.inventory.mapper.StockTransferMapper;
import com.pharmacy.pos.inventory.repository.StockTransferRepository;
import com.pharmacy.pos.common.enums.TransferStatus;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockTransferService {

    private final StockTransferRepository stockTransferRepository;
    private final StockTransferMapper stockTransferMapper;

    public List<StockTransferResponse> getAll() {
        return stockTransferRepository.findAll().stream()
                .map(stockTransferMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<StockTransferResponse> getByFromBranch(Long fromBranchId) {
        return stockTransferRepository.findByFromBranchId(fromBranchId).stream()
                .map(stockTransferMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<StockTransferResponse> getByToBranch(Long toBranchId) {
        return stockTransferRepository.findByToBranchId(toBranchId).stream()
                .map(stockTransferMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<StockTransferResponse> getByBranch(Long branchId) {
        return stockTransferRepository.findByFromBranchIdOrToBranchId(branchId, branchId).stream()
                .map(stockTransferMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<StockTransferResponse> getByStatus(TransferStatus status) {
        return stockTransferRepository.findByStatus(status).stream()
                .map(stockTransferMapper::toResponse)
                .collect(Collectors.toList());
    }

    public StockTransferResponse getById(Long id) {
        StockTransfer transfer = stockTransferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock transfer not found with id: " + id));
        return stockTransferMapper.toResponse(transfer);
    }

    @Transactional
    public StockTransferResponse create(StockTransferRequest request) {
        StockTransfer transfer = stockTransferMapper.toEntity(request);
        StockTransfer saved = stockTransferRepository.save(transfer);
        return stockTransferMapper.toResponse(saved);
    }

    @Transactional
    public StockTransferResponse update(Long id, StockTransferRequest request) {
        StockTransfer transfer = stockTransferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock transfer not found with id: " + id));

        stockTransferMapper.updateEntityFromRequest(transfer, request);
        StockTransfer updated = stockTransferRepository.save(transfer);
        return stockTransferMapper.toResponse(updated);
    }

    @Transactional
    public StockTransferResponse approve(Long id, Long approvedBy) {
        StockTransfer transfer = stockTransferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock transfer not found with id: " + id));

        transfer.setStatus(TransferStatus.APPROVED);
        transfer.setApprovedBy(approvedBy);
        transfer.setApprovedAt(java.time.LocalDateTime.now());

        StockTransfer updated = stockTransferRepository.save(transfer);
        return stockTransferMapper.toResponse(updated);
    }

    @Transactional
    public StockTransferResponse receive(Long id, Long receivedBy) {
        StockTransfer transfer = stockTransferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock transfer not found with id: " + id));

        transfer.setStatus(TransferStatus.COMPLETED);
        transfer.setReceivedBy(receivedBy);
        transfer.setReceivedAt(java.time.LocalDateTime.now());

        StockTransfer updated = stockTransferRepository.save(transfer);
        return stockTransferMapper.toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        StockTransfer transfer = stockTransferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock transfer not found with id: " + id));
        stockTransferRepository.delete(transfer);
    }
}
