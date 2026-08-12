package com.pharmacy.pos.sales.mapper;

import com.pharmacy.pos.sales.dto.OrderReturnItemRequest;
import com.pharmacy.pos.sales.dto.OrderReturnItemResponse;
import com.pharmacy.pos.sales.entity.OrderReturnItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderReturnItemMapper {

    @Mapping(target = "orderReturnId", source = "orderReturn.id")
    @Mapping(target = "orderItemId", source = "orderItem.id")
    OrderReturnItemResponse toResponse(OrderReturnItem orderReturnItem);

    OrderReturnItem toEntity(OrderReturnItemRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(OrderReturnItemRequest request, @MappingTarget OrderReturnItem orderReturnItem);

    List<OrderReturnItemResponse> toResponseList(List<OrderReturnItem> orderReturnItems);
}
