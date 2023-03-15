package com.enbiz.api.common.app.controller.order;

import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enbiz.api.common.app.service.order.OrderService;
import com.enbiz.common.base.rest.Response;
import com.github.underscore.U;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/payment")
@Lazy
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

	private final OrderService orderService;
	
	@GetMapping("/kcpPayInfo")
	public Response<Map<String, Object>> kcpPayInfo() {
		return new Response<Map<String, Object>>().setPayload(orderService.kcpPayInfo());
	}
	
	@GetMapping("/inicisPayInfo")
	public Response<Map<String, Object>> inicisPayInfo() {
		return new Response<Map<String, Object>>().setPayload(orderService.inicisPayInfo());
	}
	
	@GetMapping("/socialPayInfo")
	public Response<Map<String, Object>> socialPayInfo() {
		return new Response<Map<String, Object>>().setPayload(U.objectBuilder()
				.add(orderService.payInfo())
				.add("naverPay", orderService.naverPayInfo())
				.add("tossPay", orderService.tossPayInfo())
				.build());
	}

	@PostMapping("/kakaoPayReady")
	public Response<Map<String, Object>> kakaoPayReady(@RequestBody Map<String, Object> params) {
		log.debug("params: {}", params);
		
		return new Response<Map<String, Object>>().setPayload(U.objectBuilder()
				.add(orderService.payInfo())
				.add("kakaoPay", orderService.kakaoPayReady(params))
				.build());
	}
	
	@PostMapping("/approve")
	public Response<Map<String, Object>> approve(@RequestBody Map<String, Object> params) {
		log.debug("params: {}", params.keySet());
		log.debug("params.authToken: {}", params.get("authToken"));

		return new Response<Map<String, Object>>().setPayload(orderService.saveOrder(params));
	}
	
	@PostMapping("/cancel")
	public Response<Map<String, Object>> cancel(@RequestBody Map<String, Object> params) {
		log.debug("params: {}", params);
		
		orderService.cancelOrder(params);
		
		return new Response<Map<String, Object>>().setPayload(U.objectBuilder()
				.add("success", true).build());
	}
}
