package com.skyhorsemanpower.payment.payment.dto;

import com.skyhorsemanpower.payment.common.PaymentStatus;
import com.skyhorsemanpower.payment.payment.vo.PaymentDetailResponseVo;
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
	private double price;
	private String paymentNumber;
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
			paymentDetailResponseDto.getPaymentNumber(),
			paymentDetailResponseDto.getPaymentStatus(),
			paymentDetailResponseDto.getPaymentAt(),
			paymentDetailResponseDto.getPaymentCompletionAt());
	}
}