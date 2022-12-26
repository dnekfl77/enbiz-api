package com.x2bee.api.common.app.service.delivery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.x2bee.api.common.app.dto.request.delivery.SweetTrackerCallBackRequest;
import com.x2bee.api.common.app.dto.response.delivery.SweetTrackerCallBackResponse;
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
public class DeliveryApiServiceImpl implements DeliveryApiService {
	private final InfLogService infLogService;
	private static final String INF_TYP_CD = "DELIVERY_C";
	private static final String IF_AA_015 = "IF_AA_015";
	
	@Value("${app.apiUrl.bo}")
	private String apiBo;
	@Value("${app.apiUrl.bo.stcallback}")
	private String apiBoStCallBack;
	
	@Override
	public Response<SweetTrackerCallBackResponse> sweetTrackerCallBack(SweetTrackerCallBackRequest sweetTrackerCallBackRequest) throws Exception {
		StringBuilder url = new StringBuilder();
		url.append(apiBo).append(apiBoStCallBack);
		
		// 요청로그생성
		StInfLog stInfLog =  infLogService.registerInboundInfReqLog(INF_TYP_CD, IF_AA_015, HttpMethod.POST, url.toString(), sweetTrackerCallBackRequest);

		RestResponse<Response<SweetTrackerCallBackResponse>> response = null;
		try {
			// api 호출 및 오류 처리
			response = RestApi.client(url.toString()).post(sweetTrackerCallBackRequest, new ParameterizedTypeReference<Response<SweetTrackerCallBackResponse>>() {});
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
