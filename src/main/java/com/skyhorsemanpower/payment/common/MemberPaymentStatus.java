package com.skyhorsemanpower.payment.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberPaymentStatus {
	AGREE,
	DISAGREE,
	READY
}
