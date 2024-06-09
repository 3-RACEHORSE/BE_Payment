package com.skyhorsemanpower.payment.payment.domain;

import com.skyhorsemanpower.payment.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;

@Entity
@Getter
@Table(name = "skyhorse_bank")
public class Bank extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_id")
    private Long id;
    @Column(name = "auction_uuid", nullable = false, length = 23)
    private String auctionUuid;
    @Column(name = "donation", nullable = false, length = 100)
    private BigDecimal donation;
}
