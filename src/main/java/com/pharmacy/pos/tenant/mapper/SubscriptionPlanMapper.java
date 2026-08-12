package com.pharmacy.pos.tenant.mapper;

import com.pharmacy.pos.tenant.dto.SubscriptionPlanRequest;
import com.pharmacy.pos.tenant.dto.SubscriptionPlanResponse;
import com.pharmacy.pos.tenant.entity.SubscriptionPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SubscriptionPlanMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    SubscriptionPlan toEntity(SubscriptionPlanRequest request);

    @Mapping(target = "organizationId", expression = "java(entity.getOrganization() != null ? entity.getOrganization().getId() : null)")
    SubscriptionPlanResponse toResponse(SubscriptionPlan entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(SubscriptionPlanRequest request, @MappingTarget SubscriptionPlan entity);
}
