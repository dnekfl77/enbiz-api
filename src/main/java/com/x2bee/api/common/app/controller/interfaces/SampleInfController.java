package com.x2bee.api.common.app.controller.interfaces;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.x2bee.api.common.app.dto.request.sample.SampleRequest;
import com.x2bee.api.common.app.dto.response.sample.SampleResponse;
import com.x2bee.api.common.app.service.interfaces.SampleInfService;
import com.x2bee.common.base.rest.Response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/interfaces/member/samples")
@Lazy
@Slf4j
@RequiredArgsConstructor
public class SampleInfController {
	private final SampleInfService sampleInfService;
	
	@GetMapping("/search")
	public Response<List<SampleResponse>> searchSamples(@RequestBody SampleRequest sampleRequest) throws Exception {
		return sampleInfService.searchSamples(sampleRequest);
	}

}
