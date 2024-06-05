package com.skyhorsemanpower.payment.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {

	COMPLETE, //결제 완료(판매자에게 지급)
	CANCEL, //결제 취소(둘 중에 한 명이라도 미동의 시)
	PENDING, //결제 대기(구매자가 결제)
	READY //입금 대기
}
