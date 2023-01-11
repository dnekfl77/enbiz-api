package com.x2bee.api.common.app.controller.order;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.x2bee.api.common.app.service.order.OrderService;
import com.x2bee.api.common.app.service.payment.KcpPayService;
import com.x2bee.common.base.rest.Response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;

@RestController
@RequestMapping("/payment")
@Lazy
@Slf4j
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	private final KcpPayService kcpPayService;

	@GetMapping("/fetchPayInfo")
	public Response<Map<String, Object>> fetchPayInfo() {
		log.debug("request: ");

		return new Response<Map<String, Object>>().setPayload(orderService.fetchPayInfo());
	}

	@PostMapping("/approve")
	public Response<Map<String, Object>> approve(@RequestBody Map<String, Object> params, Object a) {
		log.debug("params: {}", params);

		return new Response<Map<String, Object>>().setPayload(
				kcpPayService.payment(MapUtils.getLong(params, "good_mny"),
						MapUtils.getString(params, "enc_data"),
						MapUtils.getString(params, "enc_info"),
						MapUtils.getString(params, "tran_cd"),
						StringUtils.EMPTY));
	}
}
