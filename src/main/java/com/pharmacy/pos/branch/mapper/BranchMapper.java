package com.pharmacy.pos.branch.mapper;

import com.pharmacy.pos.branch.dto.BranchRequest;
import com.pharmacy.pos.branch.dto.BranchResponse;
import com.pharmacy.pos.branch.entity.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    @Mapping(target = "id",             ignore = true)
    @Mapping(target = "organization",   ignore = true)
    @Mapping(target = "branchSettings", ignore = true)
    @Mapping(target = "devices",        ignore = true)
    @Mapping(target = "createdAt",      ignore = true)
    @Mapping(target = "updatedAt",      ignore = true)
    Branch toEntity(BranchRequest request);

    // Entity Branch has field "isActive" (column is_active, Lombok getter isIsActive() — same bug)
    // BranchResponse has field "active" (getter isActive())
    // Fix: map entity source "active" (the Lombok getter name derived from field "isActive")
    //      to DTO target "active"
    @Mapping(target = "organizationId", source = "organization.id")
    @Mapping(target = "active",         source = "active")
    BranchResponse toResponse(Branch entity);

    @Mapping(target = "id",             ignore = true)
    @Mapping(target = "organization",   ignore = true)
    @Mapping(target = "branchSettings", ignore = true)
    @Mapping(target = "devices",        ignore = true)
    @Mapping(target = "createdAt",      ignore = true)
    @Mapping(target = "updatedAt",      ignore = true)
    @Mapping(target = "active",         ignore = true)
    void updateEntityFromRequest(BranchRequest request, @MappingTarget Branch entity);
}
