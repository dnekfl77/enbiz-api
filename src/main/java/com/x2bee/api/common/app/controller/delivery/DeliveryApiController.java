package com.x2bee.api.common.app.controller.delivery;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.x2bee.api.common.app.dto.request.delivery.SweetTrackerCallBackRequest;
import com.x2bee.api.common.app.dto.response.delivery.SweetTrackerCallBackResponse;
import com.x2bee.api.common.app.service.delivery.DeliveryApiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/delivery/api")
@Lazy
@Slf4j
@RequiredArgsConstructor
public class DeliveryApiController {
	private final DeliveryApiService deliveryApiService;
	
	@GetMapping("sweettracker-callback-test")
	public SweetTrackerCallBackResponse sweetTrackerCallBackTest(@RequestParam String invoiceNo,@RequestParam String level
			, @RequestParam String fid , @RequestParam String courierCode) throws Exception {
		SweetTrackerCallBackRequest sweetTrackerCallBackRequest = new SweetTrackerCallBackRequest();
		log.debug("invoiceNo :: " + invoiceNo);
		sweetTrackerCallBackRequest.setInvoice_no(invoiceNo);
		sweetTrackerCallBackRequest.setFid(fid);
		sweetTrackerCallBackRequest.setLevel(level);
		sweetTrackerCallBackRequest.setCourier_code(courierCode);
		return deliveryApiService.sweetTrackerCallBack(sweetTrackerCallBackRequest).getPayload();
	}
	
	@PostMapping("sweettracker-callback")
	public SweetTrackerCallBackResponse sweetTrackerCallBack(@RequestBody SweetTrackerCallBackRequest sweetTrackerCallBackRequest) throws Exception {
		return deliveryApiService.sweetTrackerCallBack(sweetTrackerCallBackRequest).getPayload();
	}
	
}
