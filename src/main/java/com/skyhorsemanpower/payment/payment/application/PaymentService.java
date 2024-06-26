package com.skyhorsemanpower.payment.payment.application;

import com.skyhorsemanpower.payment.payment.dto.PaymentAddRequestDto;
import com.skyhorsemanpower.payment.payment.dto.PaymentDetailRequestDto;
import com.skyhorsemanpower.payment.payment.dto.PaymentDetailResponseDto;
import com.skyhorsemanpower.payment.payment.dto.PaymentListResponseDto;
import com.skyhorsemanpower.payment.payment.vo.PaymentReadyVo;
import java.util.List;

public interface PaymentService {

    void createPayment(PaymentReadyVo paymentReadyVo);

    void savePayment(String uuid, String impUid, PaymentAddRequestDto paymentAddRequestDto);

    PaymentDetailResponseDto findPaymentDetail(String uuid,
        PaymentDetailRequestDto paymentDetailRequestDto);

    List<PaymentListResponseDto> findPaymentList(String uuid);

    List<PaymentListResponseDto> findCompletePayments(String auctionUuid);
}
