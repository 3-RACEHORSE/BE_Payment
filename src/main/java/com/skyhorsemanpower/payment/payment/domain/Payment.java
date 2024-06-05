package com.skyhorsemanpower.payment.payment.domain;

import com.skyhorsemanpower.payment.common.BaseCreateTimeEntity;
import com.skyhorsemanpower.payment.common.MemberPaymentStatus;
import com.skyhorsemanpower.payment.common.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "payment", uniqueConstraints = {
    @UniqueConstraint(name = "payment_uuid_unique", columnNames = {"payment_uuid"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;
    @Column(name = "payment_uuid", nullable = false, unique = true, length = 9)
    private String paymentUuid;
    @Column(name = "auction_uuid", nullable = false, unique = true, length = 23)
    private String auctionUuid;
    @Column(name = "member_uuid", nullable = false, length = 36)
    private String memberUuid;
    @Column(name = "seller_uuid", nullable = false, length = 36)
    private String sellerUuid;
    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;
    @Column(name = "payment_number", nullable = false, length = 50)
    private String paymentNumber; //카드번호나 계좌번호 같은 것
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING)
    private MemberPaymentStatus userPaymentStatus;
    @Enumerated(EnumType.STRING)
    private MemberPaymentStatus sellerPaymentStatus;
    @Column(name = "price", nullable = false, length = 100)
    private double price;
    private LocalDateTime paymentCompletionAt;

    @Builder
    public Payment(Long id, String paymentUuid, String auctionUuid, String memberUuid,
        String sellerUuid,
        String paymentMethod, String paymentNumber,
        PaymentStatus paymentStatus, MemberPaymentStatus userPaymentStatus,
        MemberPaymentStatus sellerPaymentStatus,
        double price, LocalDateTime paymentCompletionAt) {
        this.id = id;
        this.paymentUuid = paymentUuid;
        this.auctionUuid = auctionUuid;
        this.memberUuid = memberUuid;
        this.sellerUuid = sellerUuid;
        this.paymentMethod = paymentMethod;
        this.paymentNumber = paymentNumber;
        this.paymentStatus = paymentStatus;
        this.userPaymentStatus = userPaymentStatus;
        this.sellerPaymentStatus = sellerPaymentStatus;
        this.price = price;
        this.paymentCompletionAt = paymentCompletionAt;
    }
}
