package com.skyhorsemanpower.payment.payment.vo;

import lombok.Getter;

@Getter
public class PaymentAddRequestVo {

    private String auctionUuid;
    private String paymentMethod;
    private double price;
    private String paymentNumber;
}
