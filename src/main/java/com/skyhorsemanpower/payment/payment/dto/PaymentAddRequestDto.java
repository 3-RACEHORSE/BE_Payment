package com.skyhorsemanpower.payment.payment.dto;

import com.skyhorsemanpower.payment.payment.vo.PaymentAddRequestVo;
import java.math.BigDecimal;
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
public class PaymentAddRequestDto {

    private String auctionUuid;
    private String paymentMethod;
    private BigDecimal price;
    private String paymentNumber;

    public static PaymentAddRequestDto voToDto(PaymentAddRequestVo paymentAddRequestVo) {
        return PaymentAddRequestDto.builder()
            .auctionUuid(paymentAddRequestVo.getAuctionUuid())
            .paymentMethod(paymentAddRequestVo.getPaymentMethod())
            .price(paymentAddRequestVo.getPrice())
            .paymentNumber(paymentAddRequestVo.getPaymentNumber())
            .build();
    }
}
