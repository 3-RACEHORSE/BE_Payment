package com.sparos4th2.payment.payment.vo;

import com.sparos4th2.payment.payment.dto.PaymentAddRequestDto;
import lombok.Getter;

@Getter
public class PaymentAddRequestVo {

	private String auctionUuid;
	private String paymentMethod;
	private int price;
	private String paymentNumber;
}
