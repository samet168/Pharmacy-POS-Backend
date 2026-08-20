package com.pharmacy.pos.sales.mapper;

import com.pharmacy.pos.sales.dto.OrderItemRequest;
import com.pharmacy.pos.sales.dto.OrderItemResponse;
import com.pharmacy.pos.sales.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    // ── Entity → Response ─────────────────────────────────────────────────
    @Mapping(target = "orderId",      source = "order.id")
    @Mapping(target = "productId",    source = "product.id")
    @Mapping(target = "productName",  source = "product.brandName")
    @Mapping(target = "batchId",      source = "batch.id")
    @Mapping(target = "unitId",       source = "unit.id")
    OrderItemResponse toResponse(OrderItem orderItem);

    // ── Request → Entity ──────────────────────────────────────────────────
    @Mapping(target = "id",       ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "order",    ignore = true)
    @Mapping(target = "product",  ignore = true)
    @Mapping(target = "batch",    ignore = true)
    @Mapping(target = "unit",     ignore = true)
    OrderItem toEntity(OrderItemRequest request);

    @Mapping(target = "id",       ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "order",    ignore = true)
    @Mapping(target = "product",  ignore = true)
    @Mapping(target = "batch",    ignore = true)
    @Mapping(target = "unit",     ignore = true)
    void updateEntityFromRequest(OrderItemRequest request, @MappingTarget OrderItem orderItem);

    List<OrderItemResponse> toResponseList(List<OrderItem> orderItems);
}
