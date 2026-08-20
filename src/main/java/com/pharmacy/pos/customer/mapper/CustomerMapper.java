package com.pharmacy.pos.customer.mapper;

import com.pharmacy.pos.customer.dto.CustomerRequest;
import com.pharmacy.pos.customer.dto.CustomerResponse;
import com.pharmacy.pos.customer.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id",            ignore = true)
    @Mapping(target = "organization",  ignore = true)
    @Mapping(target = "allergies",     ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    @Mapping(target = "createdAt",     ignore = true)
    @Mapping(target = "updatedAt",     ignore = true)
    Customer toEntity(CustomerRequest request);

    // CustomerResponse.organizationId needs to be extracted from Customer.organization.id
    @Mapping(target = "organizationId", source = "organization.id")
    CustomerResponse toResponse(Customer entity);

    @Mapping(target = "id",            ignore = true)
    @Mapping(target = "organization",  ignore = true)
    @Mapping(target = "allergies",     ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    @Mapping(target = "imageUrl",      ignore = true)
    @Mapping(target = "createdAt",     ignore = true)
    @Mapping(target = "updatedAt",     ignore = true)
    void updateEntityFromRequest(CustomerRequest request, @MappingTarget Customer entity);
}
