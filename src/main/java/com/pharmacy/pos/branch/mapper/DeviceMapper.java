package com.pharmacy.pos.branch.mapper;

import com.pharmacy.pos.branch.dto.DeviceRequest;
import com.pharmacy.pos.branch.dto.DeviceResponse;
import com.pharmacy.pos.branch.entity.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    @Mapping(target = "id",            ignore = true)
    @Mapping(target = "branch",        ignore = true)
    @Mapping(target = "lastSyncedAt",  ignore = true)
    @Mapping(target = "registeredAt",  ignore = true)
    @Mapping(target = "createdAt",     ignore = true)
    @Mapping(target = "updatedAt",     ignore = true)
    Device toEntity(DeviceRequest request);

    // DeviceResponse has field "isActive" (primitive boolean) — Lombok getter isIsActive()
    // Device entity has field "isActive" (primitive boolean) — same Lombok issue
    // MapStruct maps by property name "active" (from isXxx getter stripping "is")
    // DeviceResponse field "isActive" → Lombok getter "isIsActive()" — MapStruct sees "isActive" property
    // Use explicit mapping to be safe
    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "active",   source = "active")   // entity "isActive" field → getter isActive() → DTO "active"
    DeviceResponse toResponse(Device entity);

    @Mapping(target = "id",           ignore = true)
    @Mapping(target = "branch",       ignore = true)
    @Mapping(target = "lastSyncedAt", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    @Mapping(target = "createdAt",    ignore = true)
    @Mapping(target = "updatedAt",    ignore = true)
    @Mapping(target = "active",       ignore = true)
    void updateEntityFromRequest(DeviceRequest request, @MappingTarget Device entity);
}
