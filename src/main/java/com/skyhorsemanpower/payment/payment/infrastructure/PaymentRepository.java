package com.skyhorsemanpower.payment.payment.infrastructure;

import com.skyhorsemanpower.payment.common.PaymentStatus;
import com.skyhorsemanpower.payment.payment.domain.Payment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	Optional<Payment> findByMemberUuidAndAuctionUuid(String memberUuid, String auctionUuid);

	List<Payment> findByMemberUuid(String memberUuid);

	Optional<Payment> findByPaymentUuid(String paymentUuid);

	List<Payment> findByAuctionUuidAndPaymentStatus (String auctionUuid, PaymentStatus paymentStatus);
}
