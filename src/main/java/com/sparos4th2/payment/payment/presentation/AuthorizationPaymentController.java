package com.sparos4th2.payment.payment.presentation;

import com.sparos4th2.payment.common.SuccessResponse;
import com.sparos4th2.payment.payment.application.PaymentService;
import com.sparos4th2.payment.payment.dto.PaymentAddRequestDto;
import com.sparos4th2.payment.payment.dto.PaymentAgreeRequestDto;
import com.sparos4th2.payment.payment.dto.PaymentDetailRequestDto;
import com.sparos4th2.payment.payment.dto.PaymentDetailResponseDto;
import com.sparos4th2.payment.payment.dto.PaymentListResponseDto;
import com.sparos4th2.payment.payment.vo.PaymentAddRequestVo;
import com.sparos4th2.payment.payment.vo.PaymentAgreeRequestVo;
import com.sparos4th2.payment.payment.vo.PaymentDetailRequestVo;
import com.sparos4th2.payment.payment.vo.PaymentDetailResponseVo;
import com.sparos4th2.payment.payment.vo.PaymentListResponseVo;
import com.sun.net.httpserver.Authenticator.Success;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "결제", description = "결제 관리 API")
@RequestMapping("/api/v1/authorization/payment")
public class AuthorizationPaymentController {

	private final PaymentService paymentService;

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
		paymentService.AgreePayment(uuid,
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
}
