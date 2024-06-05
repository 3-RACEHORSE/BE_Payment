package com.skyhorsemanpower.payment.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KafkaEntity {
    private String receiverUuid;
    private String message;
    private String eventType;
}
