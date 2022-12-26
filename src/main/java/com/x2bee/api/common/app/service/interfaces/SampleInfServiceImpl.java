package com.x2bee.api.common.app.service.interfaces;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.x2bee.api.common.app.dto.request.sample.SampleRequest;
import com.x2bee.api.common.app.dto.response.sample.SampleResponse;
import com.x2bee.api.common.app.entity.StInfLog;
import com.x2bee.api.common.app.service.interfacecommon.InfLogService;
import com.x2bee.common.base.rest.Response;
import com.x2bee.common.base.rest.RestApi;
import com.x2bee.common.base.rest.RestResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Lazy
@Slf4j
@RequiredArgsConstructor
public class SampleInfServiceImpl implements SampleInfService {
	private final InfLogService infLogService;
	
	private static final String INF_TYP_CD = "SAMPLE_INF";
	
	@Value("${app.apiUrl.member}")
	private String memberApiUrl;

	@Override
	public Response<List<SampleResponse>> searchSamples(SampleRequest sampleRequest) throws Exception {
		// api url
		String url = memberApiUrl+ "/api/member/samples/search";
		
		// 요청로그생성
		StInfLog stInfLog =  infLogService.registerInboundInfReqLog(INF_TYP_CD, "IF_SMPL_001", HttpMethod.GET, url, sampleRequest);

		RestResponse<Response<List<SampleResponse>>> response = null;
		try {
			// api 호출 및 오류 처리
			response = RestApi.client(url).get(sampleRequest, new ParameterizedTypeReference<Response<List<SampleResponse>>>() {});
			if (response.hasError()) {
				log.error("", response.getException());
				throw response.getException();
			}
			
			// 결과값 return
			return response.getBody();
		} finally {
			// 응답로그생성
			infLogService.registerInboundInfResLog(stInfLog, response);
		}
	}

}
