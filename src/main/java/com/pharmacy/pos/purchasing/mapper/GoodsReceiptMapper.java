package com.pharmacy.pos.purchasing.mapper;

import com.pharmacy.pos.purchasing.dto.GoodsReceiptRequest;
import com.pharmacy.pos.purchasing.dto.GoodsReceiptResponse;
import com.pharmacy.pos.purchasing.entity.GoodsReceipt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GoodsReceiptMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "purchaseOrder", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    GoodsReceipt toEntity(GoodsReceiptRequest request);

    GoodsReceiptResponse toResponse(GoodsReceipt entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "purchaseOrder", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(GoodsReceiptRequest request, @MappingTarget GoodsReceipt entity);
}
