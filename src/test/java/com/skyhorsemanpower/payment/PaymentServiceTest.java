//package com.skyhorsemanpower.payment;
//
//import com.skyhorsemanpower.payment.common.GenerateRandom;
//import com.skyhorsemanpower.payment.common.PaymentStatus;
//import com.skyhorsemanpower.payment.common.kafka.KafkaProducerCluster;
//import com.skyhorsemanpower.payment.common.kafka.Topics;
//import com.skyhorsemanpower.payment.payment.application.PaymentService;
//import com.skyhorsemanpower.payment.payment.domain.Payment;
//import com.skyhorsemanpower.payment.payment.dto.PaymentAddRequestDto;
//import com.skyhorsemanpower.payment.payment.dto.PaymentCompleteDto;
//import com.skyhorsemanpower.payment.payment.infrastructure.PaymentRepository;
//import com.skyhorsemanpower.payment.payment.vo.PaymentReadyVo;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class PaymentServiceTest {
//    @Autowired
//    private PaymentRepository paymentRepository;
//    @Autowired
//    private PaymentService paymentService;
//    @Mock
//    private KafkaProducerCluster producer;
//    private String paymentUuid;
//    private String auctionUuid;
//    private List<String> memberUuids = new ArrayList<>();
//
//    @BeforeEach
//    public void setup() {
//        this.paymentUuid = GenerateRandom.paymentUuid();
//        this.auctionUuid = GenerateRandom.auctionUuid();
//        for (int i = 0; i < 5;i++) {
//            this.memberUuids.add(GenerateRandom.memberUuid());
//        }
//    }
//
//    @Test
//    void createPaymentTest() {
//        for (int i = 0; i < 5; i++) {
//            PaymentReadyVo paymentReadyVo = PaymentReadyVo.builder()
//                .auctionUuid(GenerateRandom.auctionUuid())
//                .memberUuids(memberUuids)
//                .price(BigDecimal.valueOf(10000))
//                .build();
//
//            paymentService.createPayment(paymentReadyVo);
//        }
//    }
//
//    @Test
//    void savePaymentTest() {
//        List<String> myMemberUuids = List.of(
//            "53fb9b83-1715-47a6-9621-f1f8ec16a865",
//            "008808d4-37ee-40ba-8f18-4e2807f076e3",
//            "8f31612a-357e-4672-86d3-e9af51ac3780",
//            "cbe6da42-a8b9-427a-9894-40c09960fe33",
//            "50ecb773-fb60-468f-9318-8f6bca15b1a0"
//        );
//        for (String memberUuid : myMemberUuids) {
//            PaymentAddRequestDto paymentAddRequestDto = PaymentAddRequestDto.builder()
//                .auctionUuid("202406120835-624a2rpfdN")
//                .paymentMethod("toss")
//                .amountPaid(BigDecimal.valueOf(10000))
//                .paymentNumber("1234-56-78-901234")
//                .build();
//            this.paymentService.savePayment(memberUuid, paymentAddRequestDto);
//        }
//    }
//}
