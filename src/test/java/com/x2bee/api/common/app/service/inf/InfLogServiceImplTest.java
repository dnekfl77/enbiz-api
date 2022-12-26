package com.x2bee.api.common.app.service.inf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.x2bee.api.common.app.entity.StInfLog;
import com.x2bee.api.common.app.service.interfacecommon.InfLogService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
//@RequiredArgsConstructor
public class InfLogServiceImplTest {
	@Autowired
	private InfLogService infLogService;
	
	@Test
	void registerInfLogService() {
		// 요청로그
		StInfLog stInfLog = new StInfLog();
		stInfLog.setInfTypCd("1"); // 요청로그
		stInfLog.setInfId("TestInfId01");
		stInfLog.setReqUrl("http://test/url");
		stInfLog.setReqMethod("GET");
		stInfLog.setReqHerVal("{\"herkey01\":\"hervalue01\"}");
		stInfLog.setQryStrPrmt("{\"qrykey01\":\"qryvalue01\"}");
		stInfLog.setReqPrmtObj("{\"prmtkey01\":\"prmtvalue01\",\"prmtkey02\":\"prmtvalue02\"}");
		stInfLog.setSysRegId("testid01");
		stInfLog.setSysRegMenuId("testMenuId");
		stInfLog.setSysRegIpAddr("testIpAddr");
		infLogService.registerInfLogService(stInfLog);
		
		Assertions.assertNotNull(stInfLog.getInfLogSeq());

		// 응답로그
		//StInfLog stInfLog = new StInfLog();
		stInfLog.setInfTypCd("2"); // 응답로그
		stInfLog.setUprInfLogSeq(stInfLog.getInfLogSeq());
		stInfLog.setInfLogSeq(null);
		stInfLog.setRplyStatCd("0000");
		stInfLog.setRplyHerVal("{\"rherkey01\":\"rhervalue01\"}");
		stInfLog.setRplyCont("{\"rcontkey01\":\"rcontvalue01\",\"rcontkey02\":\"rcontvalue02\"}");
		infLogService.registerInfLogService(stInfLog);
		
		Assertions.assertNotNull(stInfLog.getInfLogSeq());
	}
	
	
}
