package com.pharmacy.pos.tenant.mapper;

import com.pharmacy.pos.tenant.dto.OrganizationRequest;
import com.pharmacy.pos.tenant.dto.OrganizationResponse;
import com.pharmacy.pos.tenant.entity.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    @Mapping(target = "id",                ignore = true)
    @Mapping(target = "subscriptionPlans", ignore = true)
    @Mapping(target = "createdAt",         ignore = true)
    @Mapping(target = "updatedAt",         ignore = true)
    Organization toEntity(OrganizationRequest request);

    // OrganizationResponse has field "isActive" (primitive boolean)
    // Organization entity has field "isActive" (primitive boolean) → Lombok getter isActive()
    // MapStruct derives property name "active" from isActive() getter
    // DTO field "isActive" → Lombok getter isIsActive() — MapStruct sees "isActive" property
    @Mapping(target = "active", source = "active")
    OrganizationResponse toResponse(Organization entity);

    @Mapping(target = "id",                ignore = true)
    @Mapping(target = "subscriptionPlans", ignore = true)
    @Mapping(target = "createdAt",         ignore = true)
    @Mapping(target = "updatedAt",         ignore = true)
    @Mapping(target = "active",            ignore = true)
    void updateEntityFromRequest(OrganizationRequest request, @MappingTarget Organization entity);
}
