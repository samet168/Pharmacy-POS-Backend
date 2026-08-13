package com.pharmacy.pos.iam.mapper;

import com.pharmacy.pos.iam.dto.UserRequest;
import com.pharmacy.pos.iam.dto.UserResponse;
import com.pharmacy.pos.iam.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "passwordHash", source = "password")
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "userBranches", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    User toEntity(UserRequest request);

    UserResponse toResponse(User entity);

    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "pinCode", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "userBranches", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateEntityFromRequest(UserRequest request, @MappingTarget User entity);
}