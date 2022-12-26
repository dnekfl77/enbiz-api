package com.x2bee.api.common.app.controller.interfacecommon;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.x2bee.api.common.app.service.interfacecommon.InfRequestService;
import com.x2bee.common.base.rest.Response;
import com.x2bee.common.base.rest.infapi.InfRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/inf-requests")
@Lazy
@Slf4j
@RequiredArgsConstructor
public class InfRequestController {
	private final InfRequestService infRequestService;

	@PostMapping("/one")
	public Response<Object> postOneRequest(@RequestBody InfRequest infRequest) {
		return new Response<Object>().setPayload(infRequestService.postOneRequest(infRequest).getBody());
	}

	@PostMapping("")
	public Response<Object> postRequest(@RequestBody InfRequest infRequest) {
		return new Response<Object>().setPayload(infRequestService.postRequest(infRequest).getBody());
	}

	@PostMapping("/text")
	public Response<Object> postTextRequest(@RequestBody InfRequest infRequest) {
		return new Response<Object>().setPayload(infRequestService.postTextRequest(infRequest).getBody());
	}

}
