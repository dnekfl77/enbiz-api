package com.enbiz.api.common.app.controller.order;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enbiz.api.common.app.dto.request.order.OrdRequest;
import com.enbiz.api.common.app.dto.response.order.OrdResponse;
import com.enbiz.api.common.app.dto.response.order.OrderPrepareResponse;
import com.enbiz.api.common.app.service.order.OrderService;
import com.enbiz.common.base.rest.Response;
import com.github.underscore.U;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/order")
@Lazy
@Slf4j
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@GetMapping("/selectOrders")
	public Response<List<OrdResponse>> selectOrders(OrdRequest request) {
		log.debug("request: {}", request);
		
		return new Response<List<OrdResponse>>().setPayload(orderService.listOrder(request));
	}

	public Response<OrderPrepareResponse> prepareOrder() {
		
		return null;
	}
	
	@PostMapping("/cancelOrder")
	public Response<Map<String, Object>> cancelOrder(@RequestBody OrdRequest request) {
		log.debug("request: {}", request);
		
		orderService.cancelOrder(request);
		
		return new Response<Map<String, Object>>().setPayload(U.objectBuilder()
				.add("success", true).build());
	}
}
