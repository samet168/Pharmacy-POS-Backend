package com.pharmacy.pos.customer.mapper;

import com.pharmacy.pos.customer.dto.PrescriptionRequest;
import com.pharmacy.pos.customer.dto.PrescriptionResponse;
import com.pharmacy.pos.customer.entity.Prescription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Prescription toEntity(PrescriptionRequest request);

    PrescriptionResponse toResponse(Prescription entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(PrescriptionRequest request, @MappingTarget Prescription entity);
}
