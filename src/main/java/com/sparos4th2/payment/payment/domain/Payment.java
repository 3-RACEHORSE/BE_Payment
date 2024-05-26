package com.sparos4th2.payment.payment.domain;

import com.sparos4th2.payment.common.BaseCreateTimeEntity;
import com.sparos4th2.payment.common.MemberPaymentStatus;
import com.sparos4th2.payment.common.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseCreateTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private Long id;
	@Column(name = "payment_uuid", nullable = false)
	private String paymentUuid;
	@Column(name = "auction_uuid", nullable = false)
	private String auctionUuid;
	@Column(name = "member_uuid", nullable = false)
	private String memberUuid;
	@Column(name = "seller_uuid", nullable = false)
	private String sellerUuid;
	@Column(name = "payment_method", nullable = false)
	private String paymentMethod;
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;
	@Enumerated(EnumType.STRING)
	private MemberPaymentStatus userPaymentStatus;
	@Enumerated(EnumType.STRING)
	private MemberPaymentStatus sellerPaymentStatus;
	@Column(name = "price", nullable = false)
	private int price;
	private LocalDateTime paymentCompletionAt;

	@Builder
	public Payment(Long id, String paymentUuid, String auctionUuid, String memberUuid, String sellerUuid,
		String paymentMethod,
		PaymentStatus paymentStatus, MemberPaymentStatus userPaymentStatus,
		MemberPaymentStatus sellerPaymentStatus,
		int price, LocalDateTime paymentCompletionAt) {
		this.id = id;
		this.paymentUuid = paymentUuid;
		this.auctionUuid = auctionUuid;
		this.memberUuid = memberUuid;
		this.sellerUuid = sellerUuid;
		this.paymentMethod = paymentMethod;
		this.paymentStatus = paymentStatus;
		this.userPaymentStatus = userPaymentStatus;
		this.sellerPaymentStatus = sellerPaymentStatus;
		this.price = price;
		this.paymentCompletionAt = paymentCompletionAt;
	}
}
