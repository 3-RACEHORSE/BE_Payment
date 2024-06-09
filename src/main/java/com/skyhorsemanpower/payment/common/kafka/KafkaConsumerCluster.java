package com.skyhorsemanpower.payment.common.kafka;

import com.skyhorsemanpower.payment.payment.application.PaymentService;
import com.skyhorsemanpower.payment.payment.vo.PaymentReadyVo;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaConsumerCluster {

    private final PaymentService paymentService;

    @KafkaListener(topics = "${spring.kafka.template.default-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(@Payload LinkedHashMap<String, Object> message,
        @Headers MessageHeaders messageHeaders) {
        log.info("consumer: success >>> message: {}, headers: {}", message.toString(),
            messageHeaders);

        //message를 PaymentReadyVo로 변환
        PaymentReadyVo paymentReadyVo = PaymentReadyVo.builder()
            .auctionUuid(message.get("auctionUuid").toString())
            .memberUuids((List<String>) message.get("memberUuids"))
            .price(BigDecimal.valueOf((Integer) message.get("price")))
            .build();

        log.info("consumer: success >>> paymentReadyVo: {}", paymentReadyVo.toString());
        
        paymentService.createPayment(paymentReadyVo);
    }

}
