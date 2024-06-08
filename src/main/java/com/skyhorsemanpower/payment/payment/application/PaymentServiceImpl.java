package com.skyhorsemanpower.payment.payment.application;

import com.skyhorsemanpower.payment.common.PaymentStatus;
import com.skyhorsemanpower.payment.common.exception.CustomException;
import com.skyhorsemanpower.payment.common.exception.ResponseStatus;
import com.skyhorsemanpower.payment.payment.domain.Payment;
import com.skyhorsemanpower.payment.payment.dto.PaymentAddRequestDto;
import com.skyhorsemanpower.payment.payment.dto.PaymentDetailRequestDto;
import com.skyhorsemanpower.payment.payment.dto.PaymentDetailResponseDto;
import com.skyhorsemanpower.payment.payment.dto.PaymentListResponseDto;
import com.skyhorsemanpower.payment.payment.infrastructure.PaymentRepository;
import com.skyhorsemanpower.payment.payment.vo.PaymentReadyVo;
import java.time.LocalDateTime;
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
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    //paymentUuid 생성
    private String createPaymentUuid() {
        String character = "0123456789";
        StringBuilder paymentUuid = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            paymentUuid.append(character.charAt(random.nextInt(character.length())));
        }
        return paymentUuid.toString();
    }

    //결제 대기 생성
    @Override
    @Transactional
    public void createPayment(PaymentReadyVo paymentReadyVo) {
        if (PaymentReadyVo.validate(paymentReadyVo)) {
            paymentReadyVo.getMemberUuids().forEach(memberUuid -> {
                try {
                    paymentRepository.save(Payment.builder()
                        .paymentUuid(createPaymentUuid())
                        .auctionUuid(paymentReadyVo.getAuctionUuid())
                        .memberUuid(memberUuid)
                        .paymentStatus(PaymentStatus.PENDING)
                        .price(paymentReadyVo.getPrice())
                        .build());
                } catch (RuntimeException exception) {
                    log.info("payment >>> paymentReadyVo: {}", exception.getMessage());
                    throw new CustomException(ResponseStatus.DATABASE_INSERT_FAIL);
                }
            });
        }

    }

    //결제
    @Override
    @Transactional
    public void savePayment(String uuid, PaymentAddRequestDto paymentAddRequestDto) {
        if (paymentRepository.findByAuctionUuid(paymentAddRequestDto.getAuctionUuid())
            .isPresent()) {
            throw new CustomException(ResponseStatus.ALREADY_PAID_AUCTION_UUID);
        }

        String paymentUuid = createPaymentUuid();

        Payment payment = Payment.builder()
            .paymentUuid(paymentUuid)
            .auctionUuid(paymentAddRequestDto.getAuctionUuid())
            .paymentMethod(paymentAddRequestDto.getPaymentMethod())
            .price(paymentAddRequestDto.getPrice())
            .memberUuid(uuid)
            .paymentNumber(paymentAddRequestDto.getPaymentNumber())
            .paymentStatus(PaymentStatus.COMPLETE)
            //Todo: 클라이언트에서 결제완료 시간 준다면 그걸로 수정
            .paymentCompletionAt(LocalDateTime.now())
            .build();

        paymentRepository.save(payment);
    }

    //결제번호 마스킹
    private String maskPaymentNumber(String paymentNumber) {
        if (paymentNumber == null || paymentNumber.length() <= 5) {
            return paymentNumber;
        }
        String firstDigit = paymentNumber.substring(0, 5);
        String maskedRest = paymentNumber.substring(5).replaceAll("\\.", "*");
        return firstDigit + maskedRest;
    }

    //결제 상세 조회
    @Override
    @Transactional(readOnly = true)
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
            .paymentAt(payment.getCreatedAt())
            .paymentCompletionAt(payment.getCompletionAt())
            .build();
    }

    //결제 리스트 조회
    @Override
    @Transactional(readOnly = true)
    public List<PaymentListResponseDto> findPaymentList(String uuid) {
        List<PaymentListResponseDto> paymentListResponseDtoList = new ArrayList<>();
        for (Payment payment : paymentRepository.findByMemberUuid(uuid)) {
            PaymentListResponseDto paymentListResponseDto = PaymentListResponseDto.builder()
                .paymentUuid(payment.getPaymentUuid())
                .auctionUuid(payment.getAuctionUuid())
                .price(payment.getPrice())
                .paymentStatus(payment.getPaymentStatus())
                .paymentAt(payment.getCreatedAt())
                .build();
            paymentListResponseDtoList.add(paymentListResponseDto);
        }
        return paymentListResponseDtoList;
    }
}
