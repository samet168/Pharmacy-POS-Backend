package com.pharmacy.pos.branch.mapper;

import com.pharmacy.pos.branch.dto.BranchSettingsRequest;
import com.pharmacy.pos.branch.dto.BranchSettingsResponse;
import com.pharmacy.pos.branch.entity.BranchSettings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BranchSettingsMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BranchSettings toEntity(BranchSettingsRequest request);

    @Mapping(target = "branchId", expression = "java(entity.getBranch() != null ? entity.getBranch().getId() : null)")
    BranchSettingsResponse toResponse(BranchSettings entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(BranchSettingsRequest request, @MappingTarget BranchSettings entity);
}
