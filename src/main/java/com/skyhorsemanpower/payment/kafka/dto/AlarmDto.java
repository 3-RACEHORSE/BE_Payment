package com.skyhorsemanpower.payment.kafka.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AlarmDto {
    private List<String> memberUuids;
    private String eventType;
    private String message;
}
