package com.pharmacy.pos.sales.mapper;

import com.pharmacy.pos.sales.dto.OrderReturnRequest;
import com.pharmacy.pos.sales.dto.OrderReturnResponse;
import com.pharmacy.pos.sales.entity.OrderReturn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderReturnMapper {

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "processedBy", ignore = true)
    OrderReturnResponse toResponse(OrderReturn orderReturn);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "processedBy", ignore = true)
    OrderReturn toEntity(OrderReturnRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "processedBy", ignore = true)
    void updateEntityFromRequest(OrderReturnRequest request, @MappingTarget OrderReturn orderReturn);

    List<OrderReturnResponse> toResponseList(List<OrderReturn> orderReturns);
}
