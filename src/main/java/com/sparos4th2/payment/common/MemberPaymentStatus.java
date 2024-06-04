package com.sparos4th2.payment.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberPaymentStatus {
	MEMBER_PAYMENT_AGREE,
	MEMBER_PAYMENT_DISAGREE,
	MEMBER_PAYMENT_READY
}
