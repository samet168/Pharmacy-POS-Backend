package com.pharmacy.pos.purchasing.mapper;

import com.pharmacy.pos.purchasing.dto.PurchaseOrderRequest;
import com.pharmacy.pos.purchasing.dto.PurchaseOrderResponse;
import com.pharmacy.pos.purchasing.entity.PurchaseOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "goodsReceipts", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PurchaseOrder toEntity(PurchaseOrderRequest request);

    PurchaseOrderResponse toResponse(PurchaseOrder entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "goodsReceipts", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(PurchaseOrderRequest request, @MappingTarget PurchaseOrder entity);
}
