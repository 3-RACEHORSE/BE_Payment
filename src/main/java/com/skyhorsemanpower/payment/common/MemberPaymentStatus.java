package com.skyhorsemanpower.payment.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberPaymentStatus {
	MEMBER_PAYMENT_AGREE,
	MEMBER_PAYMENT_DISAGREE,
	MEMBER_PAYMENT_READY
}
