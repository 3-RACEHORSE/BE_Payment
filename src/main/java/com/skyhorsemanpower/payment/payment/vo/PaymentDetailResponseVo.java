package com.skyhorsemanpower.payment.payment.vo;

import com.skyhorsemanpower.payment.common.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PaymentDetailResponseVo {

    private String paymentUuid;
    private String auctionUuid;
    private String paymentMethod;
    private String paymentNumber;
    private PaymentStatus paymentStatus;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime completionAt;

    public PaymentDetailResponseVo(
        String paymentUuid,
        String auctionUuid,
        String paymentMethod,
        String paymentNumber,
        PaymentStatus paymentStatus,
        BigDecimal price,
        LocalDateTime createdAt,
        LocalDateTime completionAt) {
        this.paymentUuid = paymentUuid;
        this.auctionUuid = auctionUuid;
        this.paymentMethod = paymentMethod;
        this.paymentNumber = paymentNumber;
        this.paymentStatus = paymentStatus;
        this.price = price;
        this.createdAt = createdAt;
        this.completionAt = completionAt;
    }
}
