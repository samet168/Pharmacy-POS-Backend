package com.pharmacy.pos.customer.mapper;

import com.pharmacy.pos.customer.dto.PrescriptionItemRequest;
import com.pharmacy.pos.customer.dto.PrescriptionItemResponse;
import com.pharmacy.pos.customer.entity.PrescriptionItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PrescriptionItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescription", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PrescriptionItem toEntity(PrescriptionItemRequest request);

    PrescriptionItemResponse toResponse(PrescriptionItem entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescription", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(PrescriptionItemRequest request, @MappingTarget PrescriptionItem entity);
}
