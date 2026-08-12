package com.pharmacy.pos.iam.mapper;

import com.pharmacy.pos.iam.dto.ShiftRequest;
import com.pharmacy.pos.iam.dto.ShiftResponse;
import com.pharmacy.pos.iam.entity.Shift;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ShiftMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "device", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Shift toEntity(ShiftRequest request);

    ShiftResponse toResponse(Shift entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "device", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(ShiftRequest request, @MappingTarget Shift entity);
}
