package com.pharmacy.pos.catalog.service;

import com.pharmacy.pos.catalog.dto.SupplierRequest;
import com.pharmacy.pos.catalog.dto.SupplierResponse;
import com.pharmacy.pos.catalog.entity.Supplier;
import com.pharmacy.pos.catalog.mapper.SupplierMapper;
import com.pharmacy.pos.catalog.repository.SupplierRepository;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public List<SupplierResponse> getAll() {
        return supplierRepository.findAll().stream()
                .map(supplierMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<SupplierResponse> getByOrganization(Long organizationId) {
        return supplierRepository.findByOrganizationId(organizationId).stream()
                .map(supplierMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<SupplierResponse> search(Long organizationId, String query) {
        return supplierRepository.findByOrganizationIdAndNameContainingIgnoreCase(organizationId, query).stream()
                .map(supplierMapper::toResponse)
                .collect(Collectors.toList());
    }

    public SupplierResponse getById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        return supplierMapper.toResponse(supplier);
    }

    @Transactional
    public SupplierResponse create(SupplierRequest request) {
        Supplier supplier = supplierMapper.toEntity(request);
        supplier.setActive(request.isActive());
        Supplier saved = supplierRepository.save(supplier);
        return supplierMapper.toResponse(saved);
    }

    @Transactional
    public SupplierResponse update(Long id, SupplierRequest request) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));

        supplierMapper.updateEntityFromRequest(supplier, request);
        Supplier updated = supplierRepository.save(supplier);
        return supplierMapper.toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));

        if (!supplier.getProducts().isEmpty()) {
            throw new IllegalStateException("Cannot delete supplier with active products");
        }

        supplierRepository.delete(supplier);
    }
}