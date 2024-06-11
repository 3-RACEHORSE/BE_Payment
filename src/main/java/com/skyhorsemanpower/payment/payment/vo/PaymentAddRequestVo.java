package com.skyhorsemanpower.payment.payment.vo;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class PaymentAddRequestVo {

    private String auctionUuid;
    private String paymentMethod;
    private BigDecimal paidPrice;
    private String paymentNumber;
}
