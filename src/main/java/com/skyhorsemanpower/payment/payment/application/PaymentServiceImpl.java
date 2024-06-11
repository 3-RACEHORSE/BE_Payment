package com.skyhorsemanpower.payment.payment.application;

import com.skyhorsemanpower.payment.common.PaymentStatus;
import com.skyhorsemanpower.payment.common.exception.CustomException;
import com.skyhorsemanpower.payment.common.exception.ResponseStatus;
import com.skyhorsemanpower.payment.common.kafka.KafkaProducerCluster;
import com.skyhorsemanpower.payment.common.kafka.Topics;
import com.skyhorsemanpower.payment.payment.domain.Payment;
import com.skyhorsemanpower.payment.payment.dto.PaymentAddRequestDto;
import com.skyhorsemanpower.payment.payment.dto.PaymentCompleteDto;
import com.skyhorsemanpower.payment.payment.dto.PaymentDetailRequestDto;
import com.skyhorsemanpower.payment.payment.dto.PaymentDetailResponseDto;
import com.skyhorsemanpower.payment.payment.dto.PaymentListResponseDto;
import com.skyhorsemanpower.payment.payment.infrastructure.PaymentRepository;
import com.skyhorsemanpower.payment.payment.vo.PaymentReadyVo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final KafkaProducerCluster producer;

    //paymentUuid 생성
    private String createPaymentUuid() {
        String character = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder paymentUuid = new StringBuilder();
        Random random = new Random();
        paymentUuid.append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        paymentUuid.append("-");
        for (int i = 0; i < 9; i++) {
            paymentUuid.append(character.charAt(random.nextInt(character.length())));
        }

        return paymentUuid.toString();
    }

    //결제 대기 생성
    // 사용자마다 다른 paymentUuid 생성
    @Override
    @Transactional
    public void createPayment(PaymentReadyVo paymentReadyVo) {
        if (PaymentReadyVo.validate(paymentReadyVo)) {
            paymentReadyVo.getMemberUuids().forEach(memberUuid -> {
                try {
                    String paymentUuid = createPaymentUuid();
                    paymentRepository.save(Payment.builder()
                        .paymentUuid(paymentUuid)
                        .auctionUuid(paymentReadyVo.getAuctionUuid())
                        .memberUuid(memberUuid)
                        .paymentStatus(PaymentStatus.PENDING)
                        .price(paymentReadyVo.getPrice())
                        .build());
                } catch (RuntimeException exception) {
                    log.info("createPayment error message: {}", exception.getMessage());
                    throw new CustomException(ResponseStatus.DATABASE_INSERT_FAIL);
                }
            });
        }
    }

    //결제
    @Override
    @Transactional
    public void savePayment(String memberUuid, PaymentAddRequestDto paymentAddRequestDto) {
        try {
            //결제 대기 상태인 Payment 엔티티 조회
            Payment pendingPayment = getPendingPayment(memberUuid,
                paymentAddRequestDto.getAuctionUuid());

            //컬럼이 null인 필드만 요청 값으로 채움
            Payment payment = Payment.builder()
                .id(pendingPayment.getId())
                .paymentUuid(pendingPayment.getPaymentUuid())
                .auctionUuid(pendingPayment.getAuctionUuid())
                .memberUuid(pendingPayment.getMemberUuid())
                .paymentMethod(paymentAddRequestDto.getPaymentMethod())
                .paymentNumber(paymentAddRequestDto.getPaymentNumber())
                .paymentStatus(PaymentStatus.COMPLETE)
                .paidPrice(paymentAddRequestDto.getPaidPrice())
                //Todo: 클라이언트(서드파티 모듈)에서 결제완료 시간 준다면 그걸로 수정
                .completionAt(LocalDateTime.now())
                .build();

            this.paymentRepository.save(payment);
        } catch (RuntimeException exception) {
            log.info("paymentAddRequest error message: {}", exception.getMessage());
            throw new CustomException(ResponseStatus.DATABASE_INSERT_FAIL);
        }

        //TODO: 재시도 처리, 예외 처리 추가
        this.producer.sendMessage(Topics.CHAT_SERVICE.getTopic(), PaymentCompleteDto.builder()
            .memberUuid(memberUuid).build());
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

    private Payment getPendingPayment(String memberUuid, String auctionUuid) {
        Optional<Payment> paymentOpt = this.paymentRepository.findByAuctionUuidAndMemberUuid(
            auctionUuid, memberUuid);
        if (paymentOpt.isEmpty()) {
            throw new CustomException(ResponseStatus.DOSE_NOT_EXIST_PAYMENT);
        } else if (paymentOpt.get().getPaymentStatus() == PaymentStatus.COMPLETE) {
            throw new CustomException(ResponseStatus.ALREADY_PAID_AUCTION_UUID);
        } else if (paymentOpt.get().getPaymentStatus() == PaymentStatus.CANCEL) {
            throw new CustomException(ResponseStatus.ALREADY_CANCELED_PAYMENT);
        } else if (paymentOpt.get().getPaymentStatus() == PaymentStatus.REFUND) {
            throw new CustomException(ResponseStatus.ALREADY_REFUND_PAYMENT);
        }
        return paymentOpt.get();
    }

    //결제 리스트 조회
    @Override
    @Transactional(readOnly = true)
    public List<PaymentListResponseDto> findPaymentList(String uuid) {
        List<PaymentListResponseDto> paymentListResponseDtoList = new ArrayList<>();
        try {
            List<Payment> paymentList = paymentRepository.findByMemberUuid(uuid);
            if (paymentList.isEmpty()) {
                return paymentListResponseDtoList;
            }
            for (Payment payment : paymentList) {
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
        } catch (RuntimeException exception) {
            log.info("findPaymentList error message: {}", exception.getMessage());
            throw new CustomException(ResponseStatus.DATABASE_READ_FAIL);
        }
    }

    /**
     * PaymentDetailRequestDto에 paymentUuid가 있으면 paymentUuid로 조회합니다.
     * PaymentDetailRequestDto에 paymentUuid가 없고 auctionUuid와 memberUuid만 있다면 이 둘로 조회합니다.
     * */
    @Override
    @Transactional(readOnly = true)
    public PaymentDetailResponseDto findPaymentDetail(String memberUuid,
        PaymentDetailRequestDto paymentDetailRequestDto) {
        Payment payment = null;

        if (paymentDetailRequestDto.getPaymentUuid() != null) {
            payment = paymentRepository.findByPaymentUuid(
                    paymentDetailRequestDto.getPaymentUuid())
                .orElseThrow(() -> new CustomException(ResponseStatus.DOSE_NOT_EXIST_PAYMENT));
        } else if (paymentDetailRequestDto.getAuctionUuid() != null) {
            payment = paymentRepository.findByAuctionUuidAndMemberUuid(
                    paymentDetailRequestDto.getAuctionUuid(), memberUuid)
                .orElseThrow(() -> new CustomException(ResponseStatus.DOSE_NOT_EXIST_PAYMENT));
        }

        if (payment == null) {
            throw new CustomException(ResponseStatus.DOSE_NOT_EXIST_PAYMENT);
        }

        return PaymentDetailResponseDto.builder()
            .paymentUuid(payment.getPaymentUuid())
            .auctionUuid(payment.getAuctionUuid())
            .paymentMethod(payment.getPaymentMethod())
            .paymentNumber(payment.getPaymentNumber())
            .paymentStatus(payment.getPaymentStatus())
            .price(payment.getPrice())
            .createdAt(payment.getCreatedAt())
            .completionAt(payment.getCompletionAt())
            .build();
    }
}
