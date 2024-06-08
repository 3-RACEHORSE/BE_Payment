package com.skyhorsemanpower.payment.payment.domain;

import com.skyhorsemanpower.payment.common.BaseCreateTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

@Entity
@Getter
@Table(name = "skyhorse_bank", uniqueConstraints = {
    @UniqueConstraint(name = "payment_uuid_unique", columnNames = {"payment_uuid"})})
public class SkyhorseBank extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skyhorse_bank_id")
    private Long id;
    @Column(name = "member_uuid", nullable = false, length = 36)
    private String memberUuid;
    @Column(name = "balance", nullable = false, length = 100)
    private double balance;
    @Column(name = "is_withdraw", nullable = false, length = 1)
    private String isWithdraw;
}
