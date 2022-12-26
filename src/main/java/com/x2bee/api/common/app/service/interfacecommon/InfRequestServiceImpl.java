package com.x2bee.api.common.app.service.interfacecommon;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.x2bee.api.common.app.entity.StInfLog;
import com.x2bee.common.base.rest.Response;
import com.x2bee.common.base.rest.RestResponse;
import com.x2bee.common.base.rest.infapi.InfRequest;
import com.x2bee.common.base.util.JsonUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Lazy
@Slf4j
@RequiredArgsConstructor
public class InfRequestServiceImpl implements InfRequestService {
	private final InfLogService infLogService;
	private final InfRestApi infRestApi;
	
	@Override
	public RestResponse<LinkedHashMap<String,Object>> postOneRequest(InfRequest ifRequest) {
		// call request
		StInfLog stInfLog = registerInfReqLog(ifRequest, HttpMethod.resolve(ifRequest.getHttpMethod()));
		RestResponse<LinkedHashMap<String,Object>> response =  infRestApi.execute(ifRequest, HttpMethod.resolve(ifRequest.getHttpMethod()), new ParameterizedTypeReference<LinkedHashMap<String,Object>>() {});
		registerInfResLog(stInfLog, response);
		return response;
	}

	@Override
	public RestResponse<LinkedHashSet<Object>> postRequest(InfRequest infRequest) {
		// call request
		StInfLog stInfLog = registerInfReqLog(infRequest, HttpMethod.resolve(infRequest.getHttpMethod()));
		RestResponse<LinkedHashSet<Object>> response =  infRestApi.execute(infRequest, HttpMethod.resolve(infRequest.getHttpMethod()), new ParameterizedTypeReference<LinkedHashSet<Object>>() {});
		registerInfResLog(stInfLog, response);
		return response;
	}

	@Override
	public RestResponse<String> postTextRequest(InfRequest infRequest) {
		// call request
		StInfLog stInfLog = registerInfReqLog(infRequest, HttpMethod.resolve(infRequest.getHttpMethod()));
		RestResponse<String> response =  infRestApi.execute(infRequest, HttpMethod.resolve(infRequest.getHttpMethod()), String.class);
		registerInfResLog(stInfLog, response);
		return response;
	}

	private StInfLog registerInfReqLog(InfRequest request, HttpMethod requestMethod) {
		// 요청로그
		StInfLog stInfLog = new StInfLog();
		try {
			stInfLog.setInfTypCd(request.getIfType()); // 요청로그
			stInfLog.setReqRplyGbCd("1"); // 요청로그
			stInfLog.setInfId(request.getIfId());
			stInfLog.setReqUrl(request.getUrl());
			stInfLog.setReqMethod(requestMethod.name());
			stInfLog.setReqHerVal(JsonUtils.string(request.getHeaders()));
			stInfLog.setQryStrPrmt(JsonUtils.string(request.getQueryParams()));
			stInfLog.setReqPrmtObj(JsonUtils.string(request.getRequestObject()));
			stInfLog.setSysRegId(request.getSysRegId() == null ? "EMPTY" : request.getSysRegId());
			stInfLog.setSysRegMenuId(request.getSysRegMenuId() == null ? "EMPTY" : request.getSysRegMenuId());
			stInfLog.setSysRegIpAddr(request.getSysRegIpAddr() == null ? "EMPTY" : request.getSysRegIpAddr());
			infLogService.registerInfLogService(stInfLog);
		} catch (Exception e) {
			// 로그 작성 중 exception은 무시하도록 처리한다.
			log.error(e.getMessage(), e);
		}
		
		return stInfLog;
	}

	private StInfLog registerInfResLog(StInfLog stInfLog, RestResponse<?> response) {
		try {
			stInfLog.setInfTypCd("2"); // 응답로그
			stInfLog.setUprInfLogSeq(stInfLog.getInfLogSeq());
			stInfLog.setInfLogSeq(null);
			if (response != null) {
				stInfLog.setRplyStatCd(StringUtils.substring(String.valueOf(response.getStatusCode()), 0, 3));
				stInfLog.setRplyHerVal(JsonUtils.string(response.getHeaders()));
				stInfLog.setRplyCont(JsonUtils.string(response.getBody()));
			}
			infLogService.registerInfLogService(stInfLog);
		} catch (Exception e) {
			// 로그 작성 중 exception은 무시하도록 처리한다.
			log.error(e.getMessage(), e);
		}
		
		return stInfLog;
	}

}
