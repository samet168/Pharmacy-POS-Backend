package com.pharmacy.pos.iam.mapper;

import com.pharmacy.pos.iam.dto.PermissionRequest;
import com.pharmacy.pos.iam.dto.PermissionResponse;
import com.pharmacy.pos.iam.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "rolePermissions", ignore = true)
    Permission toEntity(PermissionRequest request);

    PermissionResponse toResponse(Permission entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "rolePermissions", ignore = true)
    void updateEntityFromRequest(PermissionRequest request, @MappingTarget Permission entity);
}
