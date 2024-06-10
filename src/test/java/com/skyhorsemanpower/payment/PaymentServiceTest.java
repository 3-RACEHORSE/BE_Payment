package com.skyhorsemanpower.payment;

import static org.assertj.core.api.Assertions.assertThat;

import com.skyhorsemanpower.payment.common.GenerateRandom;
import com.skyhorsemanpower.payment.common.kafka.KafkaProducerCluster;
import com.skyhorsemanpower.payment.payment.application.PaymentService;
import com.skyhorsemanpower.payment.payment.application.PaymentServiceImpl;
import com.skyhorsemanpower.payment.payment.dto.PaymentDetailResponseDto;
import com.skyhorsemanpower.payment.payment.infrastructure.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PaymentServiceTest {

    private PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
    private KafkaProducerCluster producer = Mockito.mock(KafkaProducerCluster.class);
    private PaymentService paymentService = new PaymentServiceImpl(paymentRepository, producer);

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
    void paymentDetailNullTest() {
        PaymentDetailResponseDto detailDto = PaymentDetailResponseDto.builder().build();
        System.out.println(detailDto.getPaymentUuid());
        assertThat(detailDto.getPaymentNumber()).isNull();
    }
}
