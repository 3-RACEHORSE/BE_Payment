package com.skyhorsemanpower.payment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.skyhorsemanpower.payment.common.GenerateRandom;
import com.skyhorsemanpower.payment.common.PaymentStatus;
import com.skyhorsemanpower.payment.payment.application.PaymentService;
import com.skyhorsemanpower.payment.payment.application.PaymentServiceImpl;
import com.skyhorsemanpower.payment.payment.domain.Payment;
import com.skyhorsemanpower.payment.payment.infrastructure.PaymentRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @Test
    @DisplayName("경매 결제 내역이 존재하면 true를 반환한다.")
    void paymentExistTest() {
        //given
        Payment payment = Payment.builder()
            .id(1L)
            .paymentUuid(paymentUuid)
            .auctionUuid(auctionUuid)
            .memberUuid(memberUuid)
            .sellerUuid(sellerUuid)
            .paymentMethod("toss")
            .paymentNumber(paymentNumber)
            .paymentStatus(PaymentStatus.PENDING)
            .build();

        Mockito.when(
            paymentRepository.findByAuctionUuid(auctionUuid)
        ).thenReturn(Optional.of(payment));

        //when
        boolean isPending = this.paymentService.existPayment(auctionUuid);

        //then
        assertTrue(isPending);
    }

    @Test
    @DisplayName("경매 결제 내역이 존재하지 않으면 false를 반환한다.")
    void paymentNotExistTest() {
        //given
        Mockito.when(
            paymentRepository.findByAuctionUuid(auctionUuid)
        ).thenReturn(Optional.empty());

        //when
        boolean isPending = this.paymentService.existPayment(auctionUuid);

        //then
        assertFalse(isPending);
    }
}
