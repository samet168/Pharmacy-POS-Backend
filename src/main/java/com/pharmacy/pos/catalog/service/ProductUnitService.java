package com.pharmacy.pos.catalog.service;

import com.pharmacy.pos.catalog.dto.ProductUnitRequest;
import com.pharmacy.pos.catalog.dto.ProductUnitResponse;
import com.pharmacy.pos.catalog.entity.ProductUnit;
import com.pharmacy.pos.catalog.mapper.ProductUnitMapper;
import com.pharmacy.pos.catalog.repository.ProductUnitRepository;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductUnitService {

    private final ProductUnitRepository productUnitRepository;
    private final ProductUnitMapper productUnitMapper;

    public List<ProductUnitResponse> getAll() {
        return productUnitRepository.findAll().stream()
                .map(productUnitMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<ProductUnitResponse> getByProduct(Long productId) {
        return productUnitRepository.findByProductId(productId).stream()
                .map(productUnitMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProductUnitResponse getById(Long id) {
        ProductUnit unit = productUnitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product unit not found with id: " + id));
        return productUnitMapper.toResponse(unit);
    }

    @Transactional
    public ProductUnitResponse create(ProductUnitRequest request) {
        if (request.getBarcode() != null && productUnitRepository.existsByBarcode(request.getBarcode())) {
            throw new IllegalStateException("Barcode already exists: " + request.getBarcode());
        }

        ProductUnit unit = productUnitMapper.toEntity(request);
        ProductUnit saved = productUnitRepository.save(unit);
        return productUnitMapper.toResponse(saved);
    }

    @Transactional
    public ProductUnitResponse update(Long id, ProductUnitRequest request) {
        ProductUnit unit = productUnitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product unit not found with id: " + id));

        if (request.getBarcode() != null && !request.getBarcode().equals(unit.getBarcode())) {
            if (productUnitRepository.existsByBarcode(request.getBarcode())) {
                throw new IllegalStateException("Barcode already exists: " + request.getBarcode());
            }
        }

        productUnitMapper.updateEntityFromRequest(unit, request);
        ProductUnit updated = productUnitRepository.save(unit);
        return productUnitMapper.toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        ProductUnit unit = productUnitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product unit not found with id: " + id));
        productUnitRepository.delete(unit);
    }
}
