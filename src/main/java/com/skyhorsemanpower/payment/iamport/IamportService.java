package com.skyhorsemanpower.payment.iamport;

import com.skyhorsemanpower.payment.iamport.dto.PaymentInfoDto;

public interface IamportService {

    PaymentInfoDto getPaymentInfo(String impUid);
}
