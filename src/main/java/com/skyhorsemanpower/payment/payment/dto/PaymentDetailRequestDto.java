package com.skyhorsemanpower.payment.payment.dto;

import com.skyhorsemanpower.payment.payment.vo.PaymentDetailRequestVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetailRequestDto {

    private String paymentUuid;
    private String auctionUuid;

    public static PaymentDetailRequestDto voToDto(
        PaymentDetailRequestVo paymentDetailRequestVo) {
        return PaymentDetailRequestDto.builder()
            .paymentUuid(paymentDetailRequestVo.getPaymentUuid())
            .auctionUuid(paymentDetailRequestVo.getAuctionUuid())
            .build();
    }
}
