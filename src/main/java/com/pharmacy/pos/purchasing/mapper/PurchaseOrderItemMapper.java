package com.pharmacy.pos.purchasing.mapper;

import com.pharmacy.pos.purchasing.dto.PurchaseOrderItemRequest;
import com.pharmacy.pos.purchasing.dto.PurchaseOrderItemResponse;
import com.pharmacy.pos.purchasing.entity.PurchaseOrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PurchaseOrderItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "purchaseOrder", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PurchaseOrderItem toEntity(PurchaseOrderItemRequest request);

    PurchaseOrderItemResponse toResponse(PurchaseOrderItem entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "purchaseOrder", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(PurchaseOrderItemRequest request, @MappingTarget PurchaseOrderItem entity);
}
