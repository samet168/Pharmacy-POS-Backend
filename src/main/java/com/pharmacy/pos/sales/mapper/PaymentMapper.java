package com.pharmacy.pos.sales.mapper;

import com.pharmacy.pos.sales.dto.PaymentRequest;
import com.pharmacy.pos.sales.dto.PaymentResponse;
import com.pharmacy.pos.sales.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "orderId", source = "order.id")
    PaymentResponse toResponse(Payment payment);

    Payment toEntity(PaymentRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(PaymentRequest request, @MappingTarget Payment payment);

    List<PaymentResponse> toResponseList(List<Payment> payments);
}
