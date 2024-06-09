package com.skyhorsemanpower.payment.payment.dto;

import com.skyhorsemanpower.payment.common.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class PaymentCompleteDto {

    private String memberUuid;
    private final PaymentStatus paymentStatus = PaymentStatus.COMPLETE;
}
