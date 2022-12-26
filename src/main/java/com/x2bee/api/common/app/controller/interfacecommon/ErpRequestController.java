package com.x2bee.api.common.app.controller.interfacecommon;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.x2bee.api.common.app.service.interfacecommon.ErpRequestService;
import com.x2bee.common.base.rest.Response;
import com.x2bee.common.base.rest.RestResponse;
import com.x2bee.common.base.rest.erpapi.ErpRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/erp-requests")
@Lazy
@Slf4j
@RequiredArgsConstructor
public class ErpRequestController {
	private final ErpRequestService erpRequestService;

	@PostMapping("")
	public Response<String> getOneRequest(@RequestBody ErpRequest erpRequest) {
		RestResponse<String> result = erpRequestService.postRequest(erpRequest);
		return new Response<String>().setPayload(result.getBody());
	}
}
