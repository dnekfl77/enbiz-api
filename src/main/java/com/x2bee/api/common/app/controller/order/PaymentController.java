package com.x2bee.api.common.app.controller.order;

import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.underscore.U;
import com.x2bee.api.common.app.service.order.OrderService;
import com.x2bee.common.base.rest.Response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/payment")
@Lazy
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

	private final OrderService orderService;
	
	@GetMapping("/payInfo")
	public Response<Map<String, Object>> fetchPayInfo() {
		log.debug("request: ");

		return new Response<Map<String, Object>>().setPayload(orderService.payInfo());
	}

	@PostMapping("/approve")
	public Response<Map<String, Object>> approve(@RequestBody Map<String, Object> params) {
		log.debug("params: {}", params);

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
