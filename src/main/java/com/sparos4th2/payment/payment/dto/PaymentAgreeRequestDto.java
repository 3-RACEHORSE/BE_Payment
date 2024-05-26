package com.sparos4th2.payment.payment.dto;

import com.sparos4th2.payment.payment.vo.PaymentAgreeRequestVo;
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
public class PaymentAgreeRequestDto {

	private String paymentUuid;

	public static PaymentAgreeRequestDto voToDto(PaymentAgreeRequestVo paymentAgreeRequestVo) {
		return PaymentAgreeRequestDto.builder()
			.paymentUuid(paymentAgreeRequestVo.getPaymentUuid())
			.build();
	}
}
