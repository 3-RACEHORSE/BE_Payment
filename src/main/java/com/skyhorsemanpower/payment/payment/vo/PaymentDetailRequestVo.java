package com.skyhorsemanpower.payment.payment.vo;

import lombok.Getter;

@Getter
public class PaymentDetailRequestVo {

    private String paymentUuid; //null 가능
    private String auctionUuid;
}
