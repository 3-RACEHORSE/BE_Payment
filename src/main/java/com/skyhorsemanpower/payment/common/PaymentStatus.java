package com.skyhorsemanpower.payment.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {

	PAYMENT_COMPLETE,
	PAYMENT_FAIL,
	PAYMENT_CANCEL,
	PAYMENT_PENDING,
	PAYMENT_READY
}
