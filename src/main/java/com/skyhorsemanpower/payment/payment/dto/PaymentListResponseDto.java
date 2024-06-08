package com.skyhorsemanpower.payment.payment.dto;

import com.skyhorsemanpower.payment.common.PaymentStatus;
import com.skyhorsemanpower.payment.payment.vo.PaymentListResponseVo;
import java.time.LocalDateTime;
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
public class PaymentListResponseDto {

    private String paymentUuid;
    private String auctionUuid;
    private double price;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentAt;

    public static PaymentListResponseVo dtoToVo(PaymentListResponseDto paymentListResponseDto) {
        return new PaymentListResponseVo(
            paymentListResponseDto.getPaymentUuid(),
            paymentListResponseDto.getAuctionUuid(),
            paymentListResponseDto.getPrice(),
            paymentListResponseDto.getPaymentStatus(),
            paymentListResponseDto.getPaymentAt());
    }

}
