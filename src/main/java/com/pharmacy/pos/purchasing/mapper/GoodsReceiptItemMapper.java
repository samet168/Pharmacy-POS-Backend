package com.pharmacy.pos.purchasing.mapper;

import com.pharmacy.pos.purchasing.dto.GoodsReceiptItemRequest;
import com.pharmacy.pos.purchasing.dto.GoodsReceiptItemResponse;
import com.pharmacy.pos.purchasing.entity.GoodsReceiptItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GoodsReceiptItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "goodsReceipt", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "batch", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    GoodsReceiptItem toEntity(GoodsReceiptItemRequest request);

    GoodsReceiptItemResponse toResponse(GoodsReceiptItem entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "goodsReceipt", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "batch", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(GoodsReceiptItemRequest request, @MappingTarget GoodsReceiptItem entity);
}
