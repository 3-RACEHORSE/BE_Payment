package com.sparos4th2.payment.payment.dto;

import com.sparos4th2.payment.common.PaymentStatus;
import com.sparos4th2.payment.payment.vo.PaymentDetailResponseVo;
import java.time.LocalDateTime;
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
public class PaymentDetailResponseDto {

	private String paymentUuid;
	private String auctionUuid;
	private String paymentMethod;
	private int price;
	private PaymentStatus paymentStatus;
	private LocalDateTime paymentAt;
	private LocalDateTime paymentCompletionAt;

	public static PaymentDetailResponseVo dtoToVo(
		PaymentDetailResponseDto paymentDetailResponseDto) {
		return new PaymentDetailResponseVo(
			paymentDetailResponseDto.getPaymentUuid(),
			paymentDetailResponseDto.getAuctionUuid(),
			paymentDetailResponseDto.getPaymentMethod(),
			paymentDetailResponseDto.getPrice(),
			paymentDetailResponseDto.getPaymentStatus(),
			paymentDetailResponseDto.getPaymentAt(),
			paymentDetailResponseDto.getPaymentCompletionAt());
	}
}