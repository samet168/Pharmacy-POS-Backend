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

    // ── Entity → Response ────────────────────────────────────────────────────
    @Mapping(target = "organizationId", source = "organization.id")
    @Mapping(target = "branchId",       source = "branch.id")
    @Mapping(target = "deviceId",       source = "device.id")
    @Mapping(target = "userId",         source = "user.id")
    @Mapping(target = "customerId",     source = "customer.id")
    @Mapping(target = "shiftId",        source = "shift.id")
    @Mapping(target = "prescriptionId", source = "prescription.id")
    OrderResponse toResponse(Order order);

    // ── Request → Entity ─────────────────────────────────────────────────────
    // All relationships are loaded in CheckoutService — ignore them here
    @Mapping(target = "id",               ignore = true)
    @Mapping(target = "createdAt",        ignore = true)
    @Mapping(target = "organization",     ignore = true)
    @Mapping(target = "branch",           ignore = true)
    @Mapping(target = "device",           ignore = true)
    @Mapping(target = "user",             ignore = true)
    @Mapping(target = "customer",         ignore = true)
    @Mapping(target = "shift",            ignore = true)
    @Mapping(target = "prescription",     ignore = true)
    @Mapping(target = "syncStatus",       ignore = true)
    @Mapping(target = "createdAtDevice",  ignore = true)
    Order toEntity(OrderRequest request);

    @Mapping(target = "id",               ignore = true)
    @Mapping(target = "createdAt",        ignore = true)
    @Mapping(target = "organization",     ignore = true)
    @Mapping(target = "branch",           ignore = true)
    @Mapping(target = "device",           ignore = true)
    @Mapping(target = "user",             ignore = true)
    @Mapping(target = "customer",         ignore = true)
    @Mapping(target = "shift",            ignore = true)
    @Mapping(target = "prescription",     ignore = true)
    @Mapping(target = "syncStatus",       ignore = true)
    @Mapping(target = "createdAtDevice",  ignore = true)
    void updateEntityFromRequest(OrderRequest request, @MappingTarget Order order);

    List<OrderResponse> toResponseList(List<Order> orders);
}
