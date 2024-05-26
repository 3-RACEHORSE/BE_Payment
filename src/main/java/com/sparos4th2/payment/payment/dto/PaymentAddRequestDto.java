package com.sparos4th2.payment.payment.dto;

import com.sparos4th2.payment.payment.vo.PaymentAddRequestVo;
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
public class PaymentAddRequestDto {

	private String auctionUuid;
	private String paymentMethod;
	private int price;

	public static PaymentAddRequestDto voToDto(PaymentAddRequestVo paymentAddRequestVo) {
		return PaymentAddRequestDto.builder()
			.auctionUuid(paymentAddRequestVo.getAuctionUuid())
			.paymentMethod(paymentAddRequestVo.getPaymentMethod())
			.price(paymentAddRequestVo.getPrice())
			.build();
	}
}
