package com.skyhorsemanpower.payment.kafka;

import com.skyhorsemanpower.payment.kafka.dto.AlarmDto;
import com.skyhorsemanpower.payment.payment.application.PaymentService;
import com.skyhorsemanpower.payment.payment.dto.PaymentListResponseDto;
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
    private final KafkaProducerCluster producer;

    @KafkaListener(topics = Topics.Constant.AUCTION_CLOSE, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeBidder(@Payload LinkedHashMap<String, Object> message,
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

    @KafkaListener(topics = "event-preview-topic", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeEventPreview(@Payload LinkedHashMap<String, Object> message,
                                    @Headers MessageHeaders messageHeaders) {
        String auctionUuid = message.get("auctionUuid").toString();
        List<PaymentListResponseDto> paymentListResponseDtos = paymentService.findCompletePayments(
                auctionUuid);
        List<String> memberUuids = paymentListResponseDtos.stream()
                .map(PaymentListResponseDto::getMemberUuid).toList();

        producer.sendMessage(Topics.NOTIFICATION_SERVICE.getTopic(), AlarmDto.builder()
                .receiverUuids(memberUuids)
                .eventType("payment")
                .message("행사 시작까지 24시간 남았어요.").build());
    }
}
