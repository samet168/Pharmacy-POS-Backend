package com.pharmacy.pos.iam.mapper;

import com.pharmacy.pos.iam.dto.RoleRequest;
import com.pharmacy.pos.iam.dto.RoleResponse;
import com.pharmacy.pos.iam.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "rolePermissions", ignore = true)
    @Mapping(target = "systemRole", ignore = true)
    Role toEntity(RoleRequest request);

    RoleResponse toResponse(Role entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "rolePermissions", ignore = true)
    @Mapping(target = "systemRole", ignore = true)
    void updateEntityFromRequest(RoleRequest request, @MappingTarget Role entity);
}
