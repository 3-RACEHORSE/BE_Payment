package com.sparos4th2.payment.payment.application;

import com.sparos4th2.payment.payment.dto.PaymentAddRequestDto;
import com.sparos4th2.payment.payment.dto.PaymentAgreeRequestDto;
import com.sparos4th2.payment.payment.dto.PaymentDetailRequestDto;
import com.sparos4th2.payment.payment.dto.PaymentDetailResponseDto;
import com.sparos4th2.payment.payment.dto.PaymentListResponseDto;
import java.util.List;

public interface PaymentService {

	void savePayment(String uuid, PaymentAddRequestDto paymentAddRequestDto);

	void agreePayment(String uuid, PaymentAgreeRequestDto paymentAgreeRequestDto);

	PaymentDetailResponseDto findPaymentDetail(String uuid,
		PaymentDetailRequestDto paymentDetailRequestDto);

	List<PaymentListResponseDto> findPaymentList(String uuid);
}
