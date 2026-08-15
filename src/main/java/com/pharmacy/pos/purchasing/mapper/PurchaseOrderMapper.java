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
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "expectedDeliveryDate", ignore = true)
    PurchaseOrder toEntity(PurchaseOrderRequest request);

    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "supplier.id", target = "supplierId")
    PurchaseOrderResponse toResponse(PurchaseOrder entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "goodsReceipts", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "expectedDeliveryDate", ignore = true)
    void updateEntityFromRequest(PurchaseOrderRequest request, @MappingTarget PurchaseOrder entity);
}
