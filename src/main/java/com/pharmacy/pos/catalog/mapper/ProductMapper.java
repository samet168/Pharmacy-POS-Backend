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

    // ── Request → Entity ────────────────────────────────────────────────────
    @Mapping(target = "id",              ignore = true)
    @Mapping(target = "organization",    ignore = true)
    @Mapping(target = "genericNameId",   ignore = true)
    @Mapping(target = "category",        ignore = true)
    @Mapping(target = "defaultSupplier", ignore = true)
    @Mapping(target = "createdAt",       ignore = true)
    @Mapping(target = "updatedAt",       ignore = true)
    @Mapping(target = "productUnits",    ignore = true)
    Product toEntity(ProductRequest request);

    // ── Entity → Response ───────────────────────────────────────────────────
    // source = entity getter name, target = DTO field name
    @Mapping(target = "organizationId",      source = "organization.id")
    @Mapping(target = "genericNameId",       source = "genericNameId")   // uses map(ActiveIngredient) helper
    @Mapping(target = "categoryId",          source = "category.id")
    @Mapping(target = "defaultSupplierId",   source = "defaultSupplier.id")
    // Entity has field "active"           → isActive() getter  → DTO field "active"
    // Entity has field "controlledSubstance" → isControlledSubstance() → DTO "controlledSubstance"
    @Mapping(target = "active",              source = "active")
    @Mapping(target = "controlledSubstance", source = "controlledSubstance")
    ProductResponse toResponse(Product entity);

    // ── Update Entity ────────────────────────────────────────────────────────
    @Mapping(target = "id",              ignore = true)
    @Mapping(target = "organization",    ignore = true)
    @Mapping(target = "genericNameId",   ignore = true)
    @Mapping(target = "category",        ignore = true)
    @Mapping(target = "defaultSupplier", ignore = true)
    @Mapping(target = "imageUrl",        ignore = true)
    @Mapping(target = "createdAt",       ignore = true)
    @Mapping(target = "updatedAt",       ignore = true)
    @Mapping(target = "productUnits",    ignore = true)
    void updateEntityFromRequest(ProductRequest request, @MappingTarget Product entity);

    // ── Type conversion helpers ──────────────────────────────────────────────
    default Long map(ActiveIngredient value) {
        return value != null ? value.getId() : null;
    }

    default ActiveIngredient map(Long value) {
        return null;
    }
}
