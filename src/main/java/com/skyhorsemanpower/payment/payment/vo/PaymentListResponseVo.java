package com.skyhorsemanpower.payment.payment.vo;

import com.skyhorsemanpower.payment.common.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PaymentListResponseVo {

    private String paymentUuid;
    private String impUid;
    private String auctionUuid;
    private BigDecimal price;
    private PaymentStatus paymentStatus;
    private String paymentNumber;
    private LocalDateTime completionAt;

    public PaymentListResponseVo(String paymentUuid, String impUid, String auctionUuid,
        BigDecimal price,
        PaymentStatus paymentStatus, String paymentNumber, LocalDateTime completionAt) {
        this.paymentUuid = paymentUuid;
        this.impUid = impUid;
        this.auctionUuid = auctionUuid;
        this.price = price;
        this.paymentStatus = paymentStatus;
        this.paymentNumber = paymentNumber;
        this.completionAt = completionAt;
    }
}
