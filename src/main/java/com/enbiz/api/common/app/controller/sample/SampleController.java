package com.enbiz.api.common.app.controller.sample;

import java.util.List;

import javax.validation.Valid;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enbiz.api.common.app.dto.request.sample.SampleRequest;
import com.enbiz.api.common.app.dto.response.sample.SampleResponse;
import com.enbiz.api.common.app.entity.Sample;
import com.enbiz.api.common.app.service.sample.SampleService;
import com.enbiz.api.common.base.advice.ApiError;
import com.enbiz.common.base.exception.AppException;
import com.enbiz.common.base.rest.Response;
import com.enbiz.common.base.token.UserDetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/{siteNo}/{langCd}/samples")
@Lazy
@Slf4j
@RequiredArgsConstructor
public class SampleController {
	private final SampleService sampleService;

	@GetMapping("")
	public Response<List<SampleResponse>> getAllSamples() {
		return new Response<List<SampleResponse>>().setPayload(sampleService.getAllSamples());
	}

	@GetMapping("/{id}")
	public Response<SampleResponse> getSample(@PathVariable Long id,
			@RequestHeader(value="test-header-key1", required = false) String testHeader1) {
		log.info("id: {}, testHeader1: {}", id, testHeader1);
		return new Response<SampleResponse>().setPayload(sampleService.getSample(id));
	}

	@GetMapping("/search")
	public Response<List<SampleResponse>> searchSamples(
			@PathVariable String siteNo,
			@PathVariable String langCd,
			SampleRequest sampleRequest) {
		log.info("siteNo:{}, langCd: {}, sampleRequest: {}", 
				siteNo, langCd, sampleRequest);
		return new Response<List<SampleResponse>>().setPayload(
				sampleService.searchSamples(sampleRequest));
	}

	@PostMapping("")
	public Response<String> registerSample(@RequestBody @Valid Sample sample) throws InterruptedException {
		log.info("sampleRequest: {}", sample);

		return new Response<String>();
	}

	@PutMapping("/{id}")
	public Response<String> saveSample(@PathVariable Long id, @RequestBody SampleRequest sampleRequest) {
		log.info("id: {}, sampleRequest: {}", id, sampleRequest);

		return new Response<String>();
	}

	@PatchMapping("/{id}")
	public Response<String> modifySample(@PathVariable Long id, @RequestBody SampleRequest sampleRequest) {
		log.info("id: {}, sampleRequest: {}", id, sampleRequest);

		return new Response<String>();
	}

	@DeleteMapping("/{id}")
	public Response<String> removeSample(@PathVariable Long id) {
		log.info("id: {}", id);

		return new Response<String>();
	}

	@GetMapping("/error")
	public Response<String> getError() {
		if (true) {
			AppException.exception(ApiError.UNKNOWN);
		}

		return new Response<String>();
	}

	/**
	 * void 유형으로 응답하면 안됩니다. Response<T> 유형으로 응답 해야합니다.
	 * @param sampleParam
	 * @throws InterruptedException
	 * @deprecated
	 */
	@PostMapping("/void")
	public void registerVod(@RequestBody SampleRequest sampleRequest) throws InterruptedException {
		log.info("sampleRequest: {}", sampleRequest);
	}

	@PostMapping("/crc-cds")
	public Response<String> registerCrcCd() throws Exception {
		sampleService.registerCrcCd();
		return new Response<String>();
	}

	/**
	 * @Secured("ROLE_MEMBER") 메소드 어노테이션을 사용하면 회원 로그인된 경우만 진입되도록 처리됩니다. 
	 *  로그인 안된 상태에서 해당 메소드 호출 시 HTTP_STATUS 403 FORBIDDEN 오류가 발생합니다.<br/>
	 * 컨트롤러 메소드에서 로그인 사용자 정보를 얻기위해서 @AuthenticationPrincipal 파리미터 어노테이션을 사용하면 됩니다. 
	 *  userDetail.getMbrNo()를 통해서 회원번호 조회가 가능합니다.
	 * @param userDetail
	 * @param id
	 * @param sampleRequest
	 * @return
	 * @throws Exception
	 */
	@Secured("ROLE_MEMBER")
	@GetMapping("/secure")
	public Response<String> getSampleUser(
			@AuthenticationPrincipal UserDetail userDetail, 
			SampleRequest sampleRequest) throws Exception {
		log.info("userDetail: {}", userDetail);
		if (!userDetail.getUsername().equals(sampleRequest.getUsername())) {
			AppException.exception(ApiError.NOT_AUTHORIZED);
		}
		return new Response<String>();
	}
	
}
