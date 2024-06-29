package com.skyhorsemanpower.payment.common.swagger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PaymentEndTime {
    COMPLETE(2, 0);

    private int hour;
    private int minute;

}
