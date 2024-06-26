package com.skyhorsemanpower.payment.iamport.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentInfoDto {

    private String payMethod;
    private String payNumber;
    private BigDecimal amount;


    @Builder
    public PaymentInfoDto(String payMethod, String payNumber, BigDecimal amount) {
        this.payMethod = payMethod;
        this.payNumber = payNumber;
        this.amount = amount;
    }
}
