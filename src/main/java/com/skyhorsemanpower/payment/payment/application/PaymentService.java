package com.skyhorsemanpower.payment.payment.application;

import com.skyhorsemanpower.payment.payment.dto.PaymentAddRequestDto;
import com.skyhorsemanpower.payment.payment.dto.PaymentAgreeRequestDto;
import com.skyhorsemanpower.payment.payment.dto.PaymentDetailRequestDto;
import com.skyhorsemanpower.payment.payment.dto.PaymentDetailResponseDto;
import com.skyhorsemanpower.payment.payment.dto.PaymentListResponseDto;
import java.util.List;

public interface PaymentService {

	void savePayment(String uuid, PaymentAddRequestDto paymentAddRequestDto);

	void agreePayment(String uuid, PaymentAgreeRequestDto paymentAgreeRequestDto);

	PaymentDetailResponseDto findPaymentDetail(String uuid,
		PaymentDetailRequestDto paymentDetailRequestDto);

	List<PaymentListResponseDto> findPaymentList(String uuid);
}
