package com.skyhorsemanpower.payment.payment.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class PaymentReadyVo {
    private String auctionUuid;
    private List<String> memberUuids;
    private BigDecimal price;

    public static boolean validate(PaymentReadyVo vo) {
        return vo.getAuctionUuid() != null && !vo.getAuctionUuid().isEmpty()
            && vo.getPrice() != null && vo.getMemberUuids() != null;
    }
}
