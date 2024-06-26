package com.skyhorsemanpower.payment.payment.dto;

import com.skyhorsemanpower.payment.common.PaymentStatus;
import com.skyhorsemanpower.payment.payment.vo.PaymentListResponseVo;
import java.math.BigDecimal;
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
    private String impUid;
    private String auctionUuid;
    private String memberUuid;
    private String paymentMethod;
    private String paymentNumber;
    private PaymentStatus paymentStatus;
    private BigDecimal price;
    private BigDecimal amountPaid;
    private LocalDateTime completionAt;

    public static PaymentListResponseVo dtoToVo(PaymentListResponseDto paymentListResponseDto) {
        return new PaymentListResponseVo(
            paymentListResponseDto.getPaymentUuid(),
            paymentListResponseDto.getImpUid(),
            paymentListResponseDto.getAuctionUuid(),
            paymentListResponseDto.getPrice(),
            paymentListResponseDto.getPaymentStatus(),
            paymentListResponseDto.getPaymentNumber(),
            paymentListResponseDto.getCompletionAt());
    }

}
