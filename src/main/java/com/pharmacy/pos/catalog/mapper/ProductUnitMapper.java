package com.pharmacy.pos.catalog.mapper;

import com.pharmacy.pos.catalog.dto.ProductUnitRequest;
import com.pharmacy.pos.catalog.dto.ProductUnitResponse;
import com.pharmacy.pos.catalog.entity.ProductUnit;
import org.springframework.stereotype.Component;

@Component
public class ProductUnitMapper {

    public ProductUnit toEntity(ProductUnitRequest request) {
        ProductUnit unit = new ProductUnit();
        unit.setUnitName(request.getUnitName());
        unit.setBarcode(request.getBarcode());
        unit.setConversionFactor(request.getConversionFactor());
        unit.setBaseUnit(request.getIsBaseUnit() != null ? request.getIsBaseUnit() : false);
        unit.setCostPrice(request.getCostPrice());
        unit.setSellingPrice(request.getSellingPrice());
        return unit;
    }

    public ProductUnitResponse toResponse(ProductUnit unit) {
        ProductUnitResponse response = new ProductUnitResponse();
        response.setId(unit.getId());
        response.setProductId(unit.getProduct() != null ? unit.getProduct().getId() : null);
        response.setUnitName(unit.getUnitName());
        response.setBarcode(unit.getBarcode());
        response.setConversionFactor(unit.getConversionFactor());
        response.setBaseUnit(unit.isBaseUnit());
        response.setCostPrice(unit.getCostPrice());
        response.setSellingPrice(unit.getSellingPrice());
        response.setCreatedAt(unit.getCreatedAt());
        return response;
    }

    public void updateEntityFromRequest(ProductUnit unit, ProductUnitRequest request) {
        unit.setUnitName(request.getUnitName());
        unit.setBarcode(request.getBarcode());
        unit.setConversionFactor(request.getConversionFactor());
        unit.setBaseUnit(request.getIsBaseUnit() != null ? request.getIsBaseUnit() : false);
        unit.setCostPrice(request.getCostPrice());
        unit.setSellingPrice(request.getSellingPrice());
    }
}
