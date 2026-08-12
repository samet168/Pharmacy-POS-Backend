package com.pharmacy.pos.sales.dto;

import com.pharmacy.pos.common.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {

    @NotNull
    private Long organizationId;

    @NotNull
    private Long branchId;

    private Long deviceId;

    @NotNull
    private Long userId;

    private Long customerId;

    private Long shiftId;

    private Long prescriptionId;

    private String prescriptionUrl;

    @NotNull
    @Valid
    @NotEmpty
    private List<CheckoutItemRequest> items;

    @NotNull
    @Valid
    @NotEmpty
    private List<PaymentRequest> payments;

    private Long promotionId;

    private Integer loyaltyPointsEarned;

    private String invoiceNumber;

    private String clientUuid;
}
