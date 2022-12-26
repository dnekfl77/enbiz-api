package com.x2bee.api.common.app.service.interfacecommon;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.x2bee.api.common.app.entity.StInfLog;
import com.x2bee.api.common.app.repository.displayrwdb.inf.StInfLogTrxMapper;
import com.x2bee.common.base.rest.RestResponse;
import com.x2bee.common.base.util.JsonUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Lazy
@Slf4j
@RequiredArgsConstructor
public class InfLogSericeImpl implements InfLogService {
	private final StInfLogTrxMapper stInfLogTrxMapper;
	
	/**
	 * 인터페이스 로그 등록
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void registerInfLogService(StInfLog stInfLog) {
		try {
		stInfLogTrxMapper.insertStInfLog(stInfLog);
		} catch (Exception e) {
			// 로그 작성 중 exception은 무시하도록 처리한다.
			log.error(e.getMessage(), e);
		}
		
	}

	/**
	 * Inbound interface 요청로그 생성
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public StInfLog registerInboundInfReqLog(String infTypCd, String infId, HttpMethod requestMethod, String url, Object requestParam) {
		// 요청로그
		StInfLog stInfLog = new StInfLog();
		try {
			stInfLog.setInfTypCd(infTypCd); // 요청로그
			stInfLog.setReqRplyGbCd("1"); // 요청로그
			stInfLog.setInfId(infId);
			stInfLog.setReqUrl(url);
			stInfLog.setReqMethod(requestMethod.name());
			stInfLog.setReqPrmtObj(JsonUtils.string(requestParam));
			stInfLog.setSysRegId("INTERFACE");
			registerInfLogService(stInfLog);
		} catch (Exception e) {
			// 로그 작성 중 exception은 무시하도록 처리한다.
			log.error(e.getMessage(), e);
		}
		
		return stInfLog;
	}

	/**
	 * Inbound interface 응답로그 생성
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public StInfLog registerInboundInfResLog(StInfLog stInfLog, RestResponse<?> response) {
		try {
			stInfLog.setReqRplyGbCd("2"); // 응답로그
			stInfLog.setUprInfLogSeq(stInfLog.getInfLogSeq());
			stInfLog.setInfLogSeq(null);
				if (response != null) {
				stInfLog.setRplyStatCd(StringUtils.substring(String.valueOf(response.getStatusCode()), 0, 3));
				stInfLog.setRplyHerVal(JsonUtils.string(response.getHeaders()));
				stInfLog.setRplyCont(JsonUtils.string(response.getBody()));
			}
			registerInfLogService(stInfLog);
		} catch (Exception e) {
			// 로그 작성 중 exception은 무시하도록 처리한다.
			log.error(e.getMessage(), e);
		}
		
		return stInfLog;
	}
	
	
}
