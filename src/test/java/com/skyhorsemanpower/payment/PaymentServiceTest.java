package com.skyhorsemanpower.payment;

import static org.assertj.core.api.Assertions.assertThat;

import com.skyhorsemanpower.payment.common.GenerateRandom;
import com.skyhorsemanpower.payment.common.PaymentStatus;
import com.skyhorsemanpower.payment.payment.application.PaymentService;
import com.skyhorsemanpower.payment.payment.application.PaymentServiceImpl;
import com.skyhorsemanpower.payment.payment.domain.Payment;
import com.skyhorsemanpower.payment.payment.infrastructure.PaymentRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;

public class PaymentServiceTest {

    private PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
    private PaymentService paymentService = new PaymentServiceImpl(paymentRepository);

    private String auctionUuid;
    private String memberUuid;
    private String sellerUuid;
    private String paymentUuid;
    private String paymentNumber;

    @BeforeEach
    public void setUp() {
        this.auctionUuid = GenerateRandom.auctionUuid();
        this.memberUuid = GenerateRandom.memberUuid();
        this.sellerUuid = GenerateRandom.sellerUuid();
        this.paymentUuid = GenerateRandom.paymentUuid();
        this.paymentNumber = "9490-9464-2678-5492";
    }

    @ParameterizedTest
    @EnumSource(value = PaymentStatus.class)
    @DisplayName("경매가 결제 대기 상태인지 조회한다.")
    void paymentStatusIsPendingTest(PaymentStatus paymentStatus) {
        //given
        Payment payment = Payment.builder()
            .id(1L)
            .paymentUuid(paymentUuid)
            .auctionUuid(auctionUuid)
            .memberUuid(memberUuid)
            .sellerUuid(sellerUuid)
            .paymentMethod("toss")
            .paymentNumber(paymentNumber)
            .paymentStatus(paymentStatus)
            .build();

        Mockito.when(
            paymentRepository.findByAuctionUuid(auctionUuid)
        ).thenReturn(Optional.of(payment));

        //when
        boolean isPending = this.paymentService.isPendingPayment(auctionUuid);

        //then
        assertThat(isPending).isEqualTo(paymentStatus == PaymentStatus.PENDING);
    }
}
