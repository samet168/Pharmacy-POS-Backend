package com.pharmacy.pos.catalog.mapper;

import com.pharmacy.pos.catalog.dto.ProductRequest;
import com.pharmacy.pos.catalog.dto.ProductResponse;
import com.pharmacy.pos.catalog.entity.ActiveIngredient;
import com.pharmacy.pos.catalog.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "genericNameId", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "defaultSupplier", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "productUnits", ignore = true)
    Product toEntity(ProductRequest request);

    ProductResponse toResponse(Product entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "genericNameId", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "defaultSupplier", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "productUnits", ignore = true)
    void updateEntityFromRequest(ProductRequest request, @MappingTarget Product entity);

    default Long map(ActiveIngredient value) {
        return value != null ? value.getId() : null;
    }

    default ActiveIngredient map(Long value) {
        return null;
    }
}