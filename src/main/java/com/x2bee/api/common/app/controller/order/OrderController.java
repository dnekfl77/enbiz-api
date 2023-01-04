package com.x2bee.api.common.app.controller.order;

import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.x2bee.api.common.app.service.order.OrderService;
import com.x2bee.common.base.rest.Response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/payment")
@Lazy
@Slf4j
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@GetMapping("/fetchPayInfo")
	public Response<Map<String, Object>> fetchPayInfo() {
		log.debug("request: ");

		return new Response<Map<String, Object>>().setPayload(orderService.fetchPayInfo());
	}
}
