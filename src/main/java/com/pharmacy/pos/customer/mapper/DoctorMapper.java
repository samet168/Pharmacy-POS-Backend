package com.pharmacy.pos.customer.mapper;

import com.pharmacy.pos.customer.dto.DoctorRequest;
import com.pharmacy.pos.customer.dto.DoctorResponse;
import com.pharmacy.pos.customer.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Doctor toEntity(DoctorRequest request);

    DoctorResponse toResponse(Doctor entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(DoctorRequest request, @MappingTarget Doctor entity);
}
