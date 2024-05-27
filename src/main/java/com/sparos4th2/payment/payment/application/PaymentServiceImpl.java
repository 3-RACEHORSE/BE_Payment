package com.sparos4th2.payment.payment.application;

import com.sparos4th2.payment.common.MemberPaymentStatus;
import com.sparos4th2.payment.common.PaymentStatus;
import com.sparos4th2.payment.common.exception.CustomException;
import com.sparos4th2.payment.common.exception.ResponseStatus;
import com.sparos4th2.payment.payment.domain.Payment;
import com.sparos4th2.payment.payment.dto.PaymentAddRequestDto;
import com.sparos4th2.payment.payment.dto.PaymentAgreeRequestDto;
import com.sparos4th2.payment.payment.dto.PaymentDetailRequestDto;
import com.sparos4th2.payment.payment.dto.PaymentDetailResponseDto;
import com.sparos4th2.payment.payment.dto.PaymentListResponseDto;
import com.sparos4th2.payment.payment.infrastructure.PaymentRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;

	//uuid 생성
	private String createUuid() {
		String character = "0123456789";
		StringBuilder uuid = new StringBuilder("");
		Random random = new Random();
		for (int i = 0; i < 9; i++) {
			uuid.append(character.charAt(random.nextInt(character.length())));
		}
		return uuid.toString();
	}

	//결제
	@Override
	@Transactional
	public void savePayment(String uuid, PaymentAddRequestDto paymentAddRequestDto) {
		if (paymentRepository.findByAuctionUuid(paymentAddRequestDto.getAuctionUuid())
			.isPresent()) {
			throw new CustomException(ResponseStatus.ALREADY_PAID_AUCTION_UUID);
		}

		String paymentUuid = createUuid();

		Payment payment = Payment.builder()
			.paymentUuid(paymentUuid)
			.auctionUuid(paymentAddRequestDto.getAuctionUuid())
			.paymentMethod(paymentAddRequestDto.getPaymentMethod())
			.price(paymentAddRequestDto.getPrice())
			.memberUuid(uuid)
			.sellerUuid("sellerUuid")
			.paymentNumber(paymentAddRequestDto.getPaymentNumber())
			.paymentStatus(PaymentStatus.PAYMENT_PENDING)
			.userPaymentStatus(MemberPaymentStatus.MEMBER_PAYMENT_READY)
			.sellerPaymentStatus(MemberPaymentStatus.MEMBER_PAYMENT_READY)
			.build();

		paymentRepository.save(payment);
	}

	//결제 승인
	@Override
	@Transactional
	public void AgreePayment(String uuid, PaymentAgreeRequestDto paymentAgreeRequestDto) {
		Payment payment = paymentRepository.findByPaymentUuid(
				paymentAgreeRequestDto.getPaymentUuid())
			.orElseThrow(() -> new CustomException(ResponseStatus.DOSE_NOT_EXIST_PAYMENT));

		if (payment.getMemberUuid().equals(uuid)) {
			paymentRepository.save(Payment.builder()
				.id(payment.getId())
				.paymentUuid(payment.getPaymentUuid())
				.paymentUuid(payment.getPaymentUuid())
				.auctionUuid(payment.getAuctionUuid())
				.memberUuid(payment.getMemberUuid())
				.sellerUuid(payment.getSellerUuid())
				.paymentMethod(payment.getPaymentMethod())
				.paymentNumber(payment.getPaymentNumber())
				.paymentStatus(payment.getPaymentStatus())
				.userPaymentStatus(MemberPaymentStatus.MEMBER_PAYMENT_AGREE)
				.sellerPaymentStatus(payment.getSellerPaymentStatus())
				.price(payment.getPrice())
				.build());
		} else if (payment.getSellerUuid().equals(uuid)) {
			paymentRepository.save(Payment.builder()
				.id(payment.getId())
				.paymentUuid(payment.getPaymentUuid())
				.auctionUuid(payment.getAuctionUuid())
				.memberUuid(payment.getMemberUuid())
				.sellerUuid(payment.getSellerUuid())
				.paymentMethod(payment.getPaymentMethod())
				.paymentNumber(payment.getPaymentNumber())
				.paymentStatus(payment.getPaymentStatus())
				.userPaymentStatus(payment.getUserPaymentStatus())
				.sellerPaymentStatus(MemberPaymentStatus.MEMBER_PAYMENT_AGREE)
				.price(payment.getPrice())
				.build());
		}

		Payment savedpayment = paymentRepository.findByPaymentUuid(
				paymentAgreeRequestDto.getPaymentUuid())
			.orElseThrow(() -> new CustomException(ResponseStatus.DOSE_NOT_EXIST_PAYMENT));

		if (savedpayment.getUserPaymentStatus().equals(MemberPaymentStatus.MEMBER_PAYMENT_AGREE)
			&& savedpayment.getSellerPaymentStatus()
			.equals(MemberPaymentStatus.MEMBER_PAYMENT_AGREE)) {

			LocalDateTime currentTime = LocalDateTime.now();

			paymentRepository.save(Payment.builder()
				.id(savedpayment.getId())
				.paymentUuid(payment.getPaymentUuid())
				.auctionUuid(savedpayment.getAuctionUuid())
				.memberUuid(savedpayment.getMemberUuid())
				.sellerUuid(savedpayment.getSellerUuid())
				.paymentMethod(savedpayment.getPaymentMethod())
				.paymentNumber(payment.getPaymentNumber())
				.paymentStatus(PaymentStatus.PAYMENT_COMPLETE)
				.userPaymentStatus(savedpayment.getUserPaymentStatus())
				.sellerPaymentStatus(savedpayment.getSellerPaymentStatus())
				.price(savedpayment.getPrice())
				.paymentCompletionAt(currentTime)
				.build());
		}
	}

	//결제번호 마스킹
	private String maskPaymentNumber(String paymentNumber) {
		if (paymentNumber == null || paymentNumber.length() <= 5) {
			return paymentNumber;
		}
		String firstDigit = paymentNumber.substring(0, 5);
		String maskedRest = paymentNumber.substring(5).replaceAll(".", "*");
		return firstDigit + maskedRest;
	}

	//결제 상세 조회
	@Override
	public PaymentDetailResponseDto findPaymentDetail(String uuid,
		PaymentDetailRequestDto paymentDetailRequestDto) {
		Payment payment = paymentRepository.findByMemberUuidAndPaymentUuid(uuid,
				paymentDetailRequestDto.getPaymentUuid())
			.orElseThrow(() -> new CustomException(ResponseStatus.DOSE_NOT_EXIST_PAYMENT));

		String maskedPaymentNumber = maskPaymentNumber(payment.getPaymentNumber());

		return PaymentDetailResponseDto.builder()
			.paymentUuid(payment.getPaymentUuid())
			.auctionUuid(payment.getAuctionUuid())
			.paymentMethod(payment.getPaymentMethod())
			.price(payment.getPrice())
			.paymentNumber(maskedPaymentNumber)
			.paymentStatus(payment.getPaymentStatus())
			.paymentAt(payment.getPaymentAt())
			.paymentCompletionAt(payment.getPaymentCompletionAt())
			.build();
	}

	//결제 리스트 조회
	@Override
	public List<PaymentListResponseDto> findPaymentList(String uuid) {

		List<PaymentListResponseDto> paymentListResponseDtoList = new ArrayList<>();

		for (Payment payment : paymentRepository.findByMemberUuid(uuid)) {
			PaymentListResponseDto paymentListResponseDto = PaymentListResponseDto.builder()
				.paymentUuid(payment.getPaymentUuid())
				.auctionUuid(payment.getAuctionUuid())
				.price(payment.getPrice())
				.paymentStatus(payment.getPaymentStatus())
				.paymentAt(payment.getPaymentAt())
				.build();
			paymentListResponseDtoList.add(paymentListResponseDto);
		}

		return paymentListResponseDtoList;
	}
}
