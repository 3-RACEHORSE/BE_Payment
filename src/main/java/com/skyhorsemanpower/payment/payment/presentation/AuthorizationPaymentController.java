package com.skyhorsemanpower.payment.payment.presentation;

import com.skyhorsemanpower.payment.common.KafkaEntity;
import com.skyhorsemanpower.payment.common.KafkaProducerCluster;
import com.skyhorsemanpower.payment.payment.application.PaymentService;
import com.skyhorsemanpower.payment.payment.dto.PaymentDetailRequestDto;
import com.skyhorsemanpower.payment.common.SuccessResponse;
import com.skyhorsemanpower.payment.payment.dto.PaymentAddRequestDto;
import com.skyhorsemanpower.payment.payment.dto.PaymentAgreeRequestDto;
import com.skyhorsemanpower.payment.payment.dto.PaymentDetailResponseDto;
import com.skyhorsemanpower.payment.payment.dto.PaymentListResponseDto;
import com.skyhorsemanpower.payment.payment.vo.PaymentAddRequestVo;
import com.skyhorsemanpower.payment.payment.vo.PaymentAgreeRequestVo;
import com.skyhorsemanpower.payment.payment.vo.PaymentDetailRequestVo;
import com.skyhorsemanpower.payment.payment.vo.PaymentDetailResponseVo;
import com.skyhorsemanpower.payment.payment.vo.PaymentListResponseVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "결제", description = "결제 관리 API")
@RequestMapping("/api/v1/authorization/payment")
public class AuthorizationPaymentController {

	private final PaymentService paymentService;
	private final KafkaProducerCluster producer;

	@PostMapping
	@Operation(summary = "결제", description = "결제")
	public SuccessResponse<Object> savePayment(@RequestHeader String uuid,
		@RequestBody PaymentAddRequestVo paymentAddRequestVo) {
		paymentService.savePayment(uuid, PaymentAddRequestDto.voToDto(paymentAddRequestVo));
		return new SuccessResponse<>(null);
	}

	@PostMapping("/agree")
	@Operation(summary = "결제 승인", description = "결제 승인")
	public SuccessResponse<Object> agreePayment(@RequestHeader String uuid,
		@RequestBody PaymentAgreeRequestVo paymentAgreeRequestVo) {
		paymentService.agreePayment(uuid,
			PaymentAgreeRequestDto.voToDto(paymentAgreeRequestVo));
		return new SuccessResponse<>(null);
	}

	@PostMapping("/detail")
	@Operation(summary = "결제 내역 상세조회", description = "결제 내역 상세조회")
	public SuccessResponse<PaymentDetailResponseVo> paymentDetail(@RequestHeader String uuid,
		@RequestBody PaymentDetailRequestVo paymentDetailRequestVo) {
		return new SuccessResponse<>(
			PaymentDetailResponseDto.dtoToVo(paymentService.findPaymentDetail(uuid,
				PaymentDetailRequestDto.voToDto(paymentDetailRequestVo))));
	}

	@GetMapping("/paymentlist")
	@Operation(summary = "결제 내역 리스트조회", description = "결제 내역 리스트조회")
	public SuccessResponse<List<PaymentListResponseVo>> paymentList(
		@RequestHeader String uuid) {
		List<PaymentListResponseDto> paymentListResponseDtoList = paymentService.findPaymentList(
			uuid);
		List<PaymentListResponseVo> paymentListResponseVoList = new ArrayList<>();
		for (PaymentListResponseDto paymentListResponseDto : paymentListResponseDtoList) {
			paymentListResponseVoList.add(PaymentListResponseDto.dtoToVo(paymentListResponseDto));
		}
		return new SuccessResponse<>(paymentListResponseVoList);
	}

	@GetMapping("/is-pending")
	@Operation(summary = "결제 대기 여부 조회", description = "결제 대기 여부를 조회합니다.")
	public SuccessResponse<Boolean> isPending(@RequestParam String auctionUuid) {
		this.producer.sendMessage("kafka-json-test",
			KafkaEntity.builder()
				.receiverUuid("userUuid")
				.eventType("payment")
				.message("결제 대기 여부 조회")
				.build()
			);
		return new SuccessResponse<>(true);
	}
}
