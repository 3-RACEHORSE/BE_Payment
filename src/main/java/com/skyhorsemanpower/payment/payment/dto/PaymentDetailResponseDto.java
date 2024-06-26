package com.skyhorsemanpower.payment.payment.dto;

import com.skyhorsemanpower.payment.common.PaymentStatus;
import com.skyhorsemanpower.payment.payment.vo.PaymentDetailResponseVo;
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
public class PaymentDetailResponseDto {

    private String paymentUuid;
    private String impUid;
    private String auctionUuid;
    private String paymentMethod;
    private String paymentNumber;
    private PaymentStatus paymentStatus;
    private BigDecimal price;
    private BigDecimal amountPaid;
    private LocalDateTime createdAt;
    private LocalDateTime completionAt;

    public static PaymentDetailResponseVo dtoToVo(
        PaymentDetailResponseDto paymentDetailResponseDto
    ) {
        return new PaymentDetailResponseVo(
            paymentDetailResponseDto.getPaymentUuid(),
            paymentDetailResponseDto.getImpUid(),
            paymentDetailResponseDto.getAuctionUuid(),
            paymentDetailResponseDto.getPaymentMethod(),
            paymentDetailResponseDto.getPaymentNumber(),
            paymentDetailResponseDto.getPaymentStatus(),
            paymentDetailResponseDto.getPrice(),
            paymentDetailResponseDto.getAmountPaid(),
            paymentDetailResponseDto.getCreatedAt(),
            paymentDetailResponseDto.getCompletionAt()
        );
    }
}