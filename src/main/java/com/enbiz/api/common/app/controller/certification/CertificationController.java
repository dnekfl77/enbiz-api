package com.enbiz.api.common.app.controller.certification;

import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enbiz.api.common.app.service.certification.CertificationService;
import com.enbiz.common.base.rest.Response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/certification")
@Lazy
@Slf4j
@RequiredArgsConstructor
public class CertificationController {

	private final CertificationService certificationService;

	@GetMapping("/encryptNiceData")
	public Response<Map<String, Object>> encryptNiceData(@RequestParam Map<String, Object> params) {
		log.debug("params: {}", params);

		return new Response<Map<String, Object>>().setPayload(certificationService.encodeNiceRequestData(params));
	}

	@PostMapping("/decryptNiceData")
	public Response<Map<String, Object>> decryptNiceData(@RequestBody Map<String, Object> params) {
		log.debug("params: {}", params);

		return new Response<Map<String, Object>>().setPayload(certificationService.decodeNiceResponseData(params));
	}

}
