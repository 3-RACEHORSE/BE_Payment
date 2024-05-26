package com.sparos4th2.payment.payment.dto;

import com.sparos4th2.payment.payment.vo.PaymentAddRequestVo;
import com.sparos4th2.payment.payment.vo.PaymentDetailRequestVo;
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
public class PaymentDetailRequestDto {

	private String paymentUuid;

	public static PaymentDetailRequestDto voToDto(PaymentDetailRequestVo paymentDetailRequestVo) {
		return PaymentDetailRequestDto.builder()
			.paymentUuid(paymentDetailRequestVo.getPaymentUuid())
			.build();
	}
}
