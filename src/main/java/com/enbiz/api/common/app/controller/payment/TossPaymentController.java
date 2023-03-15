package com.enbiz.api.common.app.controller.payment;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enbiz.api.common.app.dto.request.payment.PayReadyRequest;
import com.enbiz.api.common.app.dto.request.payment.TossApproveRequest;
import com.enbiz.api.common.app.dto.request.payment.TossReadyRequest;
import com.enbiz.api.common.app.dto.response.payment.TossApproveResponse;
import com.enbiz.api.common.app.dto.response.payment.TossReadyResponse;
import com.enbiz.api.common.app.service.payment.TossPayService;
import com.enbiz.common.base.rest.Response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/payment/toss")
@Lazy
@Slf4j
@RequiredArgsConstructor
public class TossPaymentController {

	private final TossPayService tossPayService;
	
	@PostMapping("/ready")
	@Deprecated
	public Response<TossReadyResponse> ready(@RequestBody PayReadyRequest request) {
		log.debug("request: {}", request);
		
		return new Response<TossReadyResponse>()
				.setCode("0000")
				.setPayload(tossPayService.ready(TossReadyRequest.from(request)));
	}
	
	@PostMapping("/approve")
	public Response<TossApproveResponse> approve(@RequestBody TossApproveRequest request) {
		log.debug("request: {}", request);
		
		return new Response<TossApproveResponse>()
				.setCode("0000")
				.setPayload(tossPayService.approve(request));
	}
}
