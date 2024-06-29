package com.skyhorsemanpower.payment.payment.application;

import com.skyhorsemanpower.payment.common.PaymentStatus;
import com.skyhorsemanpower.payment.common.exception.CustomException;
import com.skyhorsemanpower.payment.common.exception.ResponseStatus;
import com.skyhorsemanpower.payment.common.swagger.PaymentEndTime;
import com.skyhorsemanpower.payment.iamport.IamportService;
import com.skyhorsemanpower.payment.iamport.dto.PaymentInfoDto;
import com.skyhorsemanpower.payment.payment.domain.Payment;
import com.skyhorsemanpower.payment.payment.dto.PaymentAddRequestDto;
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
    private final IamportService iamportService;

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
                    Optional<Payment> pendingPaymentOpt = paymentRepository.findByMemberUuidAndAuctionUuid(
                        memberUuid, paymentReadyVo.getAuctionUuid());
                    if (pendingPaymentOpt.isEmpty()) {
                        String paymentUuid = createPaymentUuid();
                        paymentRepository.save(Payment.builder()
                            .paymentUuid(paymentUuid)
                            .auctionUuid(paymentReadyVo.getAuctionUuid())
                            .memberUuid(memberUuid)
                            .paymentStatus(PaymentStatus.PENDING)
                            .price(paymentReadyVo.getPrice())
                            .build());
                    }
                } catch (RuntimeException exception) {
                    log.info("createPayment error message: {}", exception.getMessage());
                    throw new CustomException(ResponseStatus.DATABASE_INSERT_FAIL);
                }
            });
        }
    }

    @Override
    @Transactional
    public void savePayment(String memberUuid, String impUid,
        PaymentAddRequestDto paymentAddRequestDto) {
        try {
            Payment pendingPayment = getPendingPayment(memberUuid,
                paymentAddRequestDto.getAuctionUuid());

            if (pendingPayment.getPaymentStatus() == PaymentStatus.PENDING && isBeforeEndTime()) {
                PaymentInfoDto paymentInfoDto = iamportService.getPaymentInfo(impUid);

                Payment payment = Payment.builder()
                    .id(pendingPayment.getId())
                    .paymentUuid(pendingPayment.getPaymentUuid())
                    .impUid(impUid)
                    .auctionUuid(pendingPayment.getAuctionUuid())
                    .memberUuid(memberUuid)
                    .paymentMethod(paymentInfoDto.getPayMethod())
                    .paymentNumber(paymentInfoDto.getPayNumber())
                    .paymentStatus(PaymentStatus.COMPLETE)
                    .price(pendingPayment.getPrice())
                    .amountPaid(paymentInfoDto.getAmount())
                    .completionAt(LocalDateTime.now())
                    .build();

                this.paymentRepository.save(payment);
            }
        } catch (RuntimeException exception) {
            log.info("paymentAddRequest error message: {}", exception.getMessage());
            throw new CustomException(ResponseStatus.DATABASE_INSERT_FAIL);
        }
    }

    private boolean isBeforeEndTime() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime nextDayTime = now.toLocalDate().plusDays(1)
            .atTime(PaymentEndTime.COMPLETE.getHour(), PaymentEndTime.COMPLETE.getMinute());

        if (now.isBefore(now.toLocalDate()
            .atTime(PaymentEndTime.COMPLETE.getHour(), PaymentEndTime.COMPLETE.getMinute()))) {
            nextDayTime = now.toLocalDate()
                .atTime(PaymentEndTime.COMPLETE.getHour(), PaymentEndTime.COMPLETE.getMinute());
        }

        return now.isBefore(nextDayTime);
    }

    private Payment getPendingPayment(String memberUuid, String auctionUuid) {
        Optional<Payment> paymentOpt = this.paymentRepository.findByMemberUuidAndAuctionUuid(
            memberUuid, auctionUuid);
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

    //결제번호 마스킹
    private String maskPaymentNumber(String paymentNumber) {
        if (paymentNumber == null || paymentNumber.length() <= 5) {
            return paymentNumber;
        }
        String firstDigit = paymentNumber.substring(0, 5);
        String maskedRest = paymentNumber.substring(5).replaceAll("\\.", "*");
        return firstDigit + maskedRest;
    }

    //회원의 결제 리스트 조회
    @Override
    @Transactional(readOnly = true)
    public List<PaymentListResponseDto> findPaymentList(String memberUuid) {
        List<PaymentListResponseDto> paymentListResponseDtoList = new ArrayList<>();
        try {
            List<Payment> paymentList = paymentRepository.findByMemberUuid(memberUuid);
            if (paymentList.isEmpty()) {
                return paymentListResponseDtoList;
            }
            for (Payment payment : paymentList) {
                PaymentListResponseDto paymentListResponseDto = PaymentListResponseDto.builder()
                    .paymentUuid(payment.getPaymentUuid())
                    .impUid(payment.getImpUid())
                    .auctionUuid(payment.getAuctionUuid())
                    .paymentMethod(payment.getPaymentMethod())
                    .paymentNumber(maskPaymentNumber(payment.getPaymentNumber()))
                    .paymentStatus(payment.getPaymentStatus())
                    .price(payment.getPrice())
                    .amountPaid(payment.getAmountPaid())
                    .completionAt(payment.getCreatedAt())
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
     * PaymentDetailRequestDto에 paymentUuid가 있으면 paymentUuid로 조회합니다. PaymentDetailRequestDto에
     * paymentUuid가 없고 auctionUuid와 memberUuid만 있다면 이 둘로 조회합니다.
     */
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
            payment = paymentRepository.findByMemberUuidAndAuctionUuid(
                    memberUuid, paymentDetailRequestDto.getAuctionUuid())
                .orElseThrow(() -> new CustomException(ResponseStatus.DOSE_NOT_EXIST_PAYMENT));
        }

        if (payment == null) {
            throw new CustomException(ResponseStatus.DOSE_NOT_EXIST_PAYMENT);
        }

        return PaymentDetailResponseDto.builder()
            .paymentUuid(payment.getPaymentUuid())
            .impUid(payment.getImpUid())
            .auctionUuid(payment.getAuctionUuid())
            .paymentMethod(payment.getPaymentMethod())
            .paymentNumber(maskPaymentNumber(payment.getPaymentNumber()))
            .paymentStatus(payment.getPaymentStatus())
            .price(payment.getPrice())
            .amountPaid(payment.getAmountPaid())
            .createdAt(payment.getCreatedAt())
            .completionAt(payment.getCompletionAt())
            .build();
    }

    @Override
    public List<PaymentListResponseDto> findCompletePayments(String auctionUuid) {
        List<PaymentListResponseDto> paymentListResponseDtoList = new ArrayList<>();
        try {
            List<Payment> payments = getCompletedPayments(auctionUuid);
            if (payments.isEmpty()) {
                return paymentListResponseDtoList;
            }
            for (Payment payment : payments) {
                paymentListResponseDtoList.add(
                    PaymentListResponseDto.builder()
                        .paymentUuid(payment.getPaymentUuid())
                        .impUid(payment.getImpUid())
                        .memberUuid(payment.getMemberUuid())
                        .paymentMethod(payment.getPaymentMethod())
                        .paymentNumber(maskPaymentNumber(payment.getPaymentNumber()))
                        .paymentStatus(payment.getPaymentStatus())
                        .price(payment.getPrice())
                        .amountPaid(payment.getAmountPaid())
                        .completionAt(payment.getCompletionAt())
                        .build()
                );
            }
            return paymentListResponseDtoList;
        } catch (Exception e) {
            log.info("findCompletePayments error message: {}", e.getMessage());
            throw new CustomException(ResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<Payment> getCompletedPayments(String auctionUuid) {
        try {
            return paymentRepository.findByAuctionUuidAndPaymentStatus(
                auctionUuid, PaymentStatus.COMPLETE);
        } catch (Exception e) {
            log.info("findByAuctionUuidAndPaymentStatus error message: {}", e.getMessage());
            throw new CustomException(ResponseStatus.DATABASE_READ_FAIL);
        }
    }
}
