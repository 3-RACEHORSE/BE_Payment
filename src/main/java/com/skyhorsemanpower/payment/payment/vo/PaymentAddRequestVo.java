package com.skyhorsemanpower.payment.payment.vo;

import lombok.Getter;

@Getter
public class PaymentAddRequestVo {

	private String auctionUuid;
	private String paymentMethod;
	private int price;
	private String paymentNumber;
}
