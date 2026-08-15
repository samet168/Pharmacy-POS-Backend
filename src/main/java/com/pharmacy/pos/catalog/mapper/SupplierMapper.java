package com.pharmacy.pos.catalog.mapper;

import com.pharmacy.pos.catalog.dto.SupplierRequest;
import com.pharmacy.pos.catalog.dto.SupplierResponse;
import com.pharmacy.pos.catalog.entity.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public Supplier toEntity(SupplierRequest request) {
        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setAddress(request.getAddress());
        supplier.setTaxId(request.getTaxId());
        supplier.setActive(request.isActive());
        return supplier;
    }

    public SupplierResponse toResponse(Supplier supplier) {
        SupplierResponse response = new SupplierResponse();
        response.setId(supplier.getId());
        response.setOrganizationId(supplier.getOrganization() != null ? supplier.getOrganization().getId() : null);
        response.setName(supplier.getName());
        response.setContactPerson(supplier.getContactPerson());
        response.setPhone(supplier.getPhone());
        response.setEmail(supplier.getEmail());
        response.setAddress(supplier.getAddress());
        response.setTaxId(supplier.getTaxId());
        response.setActive(supplier.isActive());
        response.setCreatedAt(supplier.getCreatedAt());
        response.setUpdatedAt(supplier.getUpdatedAt());
        response.setUpdatedAt(supplier.getUpdatedAt());
        return response;
    }

    public void updateEntityFromRequest(Supplier supplier, SupplierRequest request) {
        supplier.setName(request.getName());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setAddress(request.getAddress());
        supplier.setTaxId(request.getTaxId());
        supplier.setActive(request.isActive());
    }
}