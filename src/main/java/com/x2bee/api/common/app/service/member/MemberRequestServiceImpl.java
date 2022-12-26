package com.x2bee.api.common.app.service.member;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.x2bee.api.common.app.dto.request.member.Pt2001Request;
import com.x2bee.api.common.app.dto.response.member.MemberInfo3Key;
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
public class MemberRequestServiceImpl implements MemberRequestService {
	private final InfLogService infLogService;
	
	//Todo api_Typ_Cd
	private static final String INF_TYP_CD = "API_PT_2001";
	
	@Value("${app.apiUrl.member}")
	private String memberApiUrl;

	@Override
	public Response<MemberInfo3Key> receiveHPtoHandsomeHsMemberInfo3key(Pt2001Request pt2001Request) throws Exception {

		// api url
		String url = memberApiUrl+ "/api/member/hpoint/hsMemberInfo3key.hd";
		
		// 요청로그생성
		//StInfLog stInfLog =  infLogService.registerInboundInfReqLog(INF_TYP_CD, "API_PT_2001"+ "", HttpMethod.GET, url, pt2001Request);
	
		RestResponse<Response<MemberInfo3Key>> response = null;
		try {
			// api 호출 및 오류 처리
			response = RestApi.client(url).post(pt2001Request, new ParameterizedTypeReference<Response<MemberInfo3Key>>() {});
			if (response.hasError()) {
				log.error("", response.getException());
				throw response.getException();
			}
			
			// 결과값 return
			
		} finally {
			// 응답로그생성
			//infLogService.registerInboundInfResLog(stInfLog, response);
		}
				
		
		// TODO Auto-generated method stub
		return response.getBody();
	}


}
