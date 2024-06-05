package com.skyhorsemanpower.payment.payment.vo;

import com.skyhorsemanpower.payment.common.PaymentStatus;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PaymentListResponseVo {

	private String paymentUuid;
	private String auctionUuid;
	private double price;
	private PaymentStatus paymentStatus;
	private LocalDateTime paymentAt;

	public PaymentListResponseVo(String paymentUuid, String auctionUuid, double price,
		PaymentStatus paymentStatus, LocalDateTime paymentAt) {
		this.paymentUuid = paymentUuid;
		this.auctionUuid = auctionUuid;
		this.price = price;
		this.paymentStatus = paymentStatus;
		this.paymentAt = paymentAt;
	}
}
