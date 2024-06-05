package com.skyhorsemanpower.payment.payment.vo;

import com.skyhorsemanpower.payment.common.PaymentStatus;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PaymentDetailResponseVo {

	private String paymentUuid;
	private String auctionUuid;
	private String paymentMethod;
	private double price;
	private String paymentNumber;
	private PaymentStatus paymentStatus;
	private LocalDateTime paymentAt;
	private LocalDateTime paymentCompletionAt;

	public PaymentDetailResponseVo(String paymentUuid, String auctionUuid, String paymentMethod,
		double price, String paymentNumber,
		PaymentStatus paymentStatus, LocalDateTime paymentAt, LocalDateTime paymentCompletionAt) {
		this.paymentUuid = paymentUuid;
		this.auctionUuid = auctionUuid;
		this.paymentMethod = paymentMethod;
		this.price = price;
		this.paymentNumber = paymentNumber;
		this.paymentStatus = paymentStatus;
		this.paymentAt = paymentAt;
		this.paymentCompletionAt = paymentCompletionAt;
	}
}
