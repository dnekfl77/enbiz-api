package com.x2bee.api.common.app.service.interfacecommon;

import org.springframework.http.HttpMethod;

import com.x2bee.api.common.app.entity.StInfLog;
import com.x2bee.common.base.rest.RestResponse;

public interface InfLogService {
	/**
	 * 인터페이스 로그 등록
	 */
	public void registerInfLogService(StInfLog stInfLog);
	/**
	 * Inbound interface 요청로그 생성
	 */
	public StInfLog registerInboundInfReqLog(String infTypCd, String infId, HttpMethod requestMethod, String url, Object requestParam);
	/**
	 * Inbound interface 응답로그 생성
	 */
	public StInfLog registerInboundInfResLog(StInfLog stInfLog, RestResponse<?> response);
}
