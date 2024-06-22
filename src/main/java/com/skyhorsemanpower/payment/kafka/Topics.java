package com.skyhorsemanpower.payment.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Topics {
    NOTIFICATION_SERVICE(Constant.NOTIFICATION_SERVICE),
    AUCTION_CLOSE_STATE(Constant.AUCTION_CLOSE),
    CHAT_SERVICE(Constant.CHAT_SERVICE);

    public static class Constant {
        public static final String NOTIFICATION_SERVICE = "alarm-topic";
        public static final String AUCTION_CLOSE = "auction-close-topic";
        public static final String CHAT_SERVICE = "chat-topic";
    }

    private final String topic;
}
