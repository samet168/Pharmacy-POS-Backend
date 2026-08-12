package com.pharmacy.pos.customer.mapper;

import com.pharmacy.pos.customer.dto.CustomerAllergyRequest;
import com.pharmacy.pos.customer.dto.CustomerAllergyResponse;
import com.pharmacy.pos.customer.entity.CustomerAllergy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerAllergyMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "ingredient", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CustomerAllergy toEntity(CustomerAllergyRequest request);

    CustomerAllergyResponse toResponse(CustomerAllergy entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "ingredient", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(CustomerAllergyRequest request, @MappingTarget CustomerAllergy entity);
}
