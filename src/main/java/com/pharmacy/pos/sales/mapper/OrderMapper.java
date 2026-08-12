package com.pharmacy.pos.sales.mapper;

import com.pharmacy.pos.sales.dto.OrderRequest;
import com.pharmacy.pos.sales.dto.OrderResponse;
import com.pharmacy.pos.sales.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "organizationId", source = "organization.id")
    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "deviceId", source = "device.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "shiftId", source = "shift.id")
    @Mapping(target = "prescriptionId", source = "prescription.id")
    OrderResponse toResponse(Order order);

    Order toEntity(OrderRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(OrderRequest request, @MappingTarget Order order);

    List<OrderResponse> toResponseList(List<Order> orders);
}
