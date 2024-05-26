package com.sparos4th2.payment.payment.infrastructure;

import com.sparos4th2.payment.payment.domain.Payment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	Optional<Payment> findByAuctionUuid(String auctionUuid);

	List<Payment> findByMemberUuid(String memberUuid);

	Optional<Payment> findBySellerUuid(String sellerUuid);

	Optional<Payment> findByPaymentUuid(String paymentUuid);

	Optional<Payment> findByMemberUuidAndPaymentUuid(String memberUuid, String paymentUuid);
}
