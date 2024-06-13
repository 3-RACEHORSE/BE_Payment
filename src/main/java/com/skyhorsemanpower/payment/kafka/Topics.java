package com.skyhorsemanpower.payment.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Topics {
    NOTIFICATION_SERVICE("alarm-topic"),
    CHAT_SERVICE("chat-topic");

    private final String topic;
}
