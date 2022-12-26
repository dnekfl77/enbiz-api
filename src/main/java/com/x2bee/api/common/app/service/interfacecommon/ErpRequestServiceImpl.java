package com.x2bee.api.common.app.service.interfacecommon;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.x2bee.api.common.app.entity.StInfLog;
import com.x2bee.common.base.rest.RestResponse;
import com.x2bee.common.base.rest.erpapi.ErpRequest;
import com.x2bee.common.base.rest.infapi.InfRequest;
import com.x2bee.common.base.util.JsonUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Lazy
@Slf4j
@RequiredArgsConstructor
public class ErpRequestServiceImpl implements ErpRequestService {
	private final InfLogService infLogService;
	private final InfRestApi infRestApi;
	
	@Override
	public RestResponse<String> postRequest(ErpRequest erpRequest) {
		// 요청로그등록
		StInfLog stInfLog = registerInfReqLog(erpRequest, HttpMethod.POST);
		
		// 요청 실행
		//RestResponse<String> response = doRequest(erpRequest);
		InfRequest infRequest = new InfRequest();
		BeanUtils.copyProperties(erpRequest, infRequest);
		RestResponse<String> response = infRestApi.execute(infRequest, HttpMethod.POST, new ParameterizedTypeReference<String>() {});

		// 응답로그등록
		if (response != null) {
			registerInfResLog(stInfLog, String.valueOf(response.getStatusCode()), response.getHeaders(), response.getBody());
		}
		else {
			registerInfResLog(stInfLog, null, null, null);
		}
		
		return response;

	}	
	
//	public RestResponse<String> doRequest(ErpRequest erpRequest) {
//		long latencyTimes = -1;
//
//
//		HttpMethod method = HttpMethod.POST;
//		ResponseEntity<String> responseEntity = null;
//		Exception exception = null;
//		URI orgUrl = null;
//		long start = System.currentTimeMillis();
//		try {
//			WebClient webClient = getWebclient(erpRequest.getConnectionTimeoutSeconds(), erpRequest.getResponseTimeoutSeconds()); // TODO 
//	
//			// 한글 파라미터 로깅을 위하여.
//			UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(erpRequest.getUrl());
//			orgUrl = uriComponentsBuilder.build().toUri(); // 한글 파라미터 로깅을 위하여.
//			uriComponentsBuilder.encode();
//			URI url = uriComponentsBuilder.build().toUri();
//
//			ResponseSpec respenseSpec;
//			respenseSpec = webClient
//				.method(method)
//				.uri(url)
//				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//				.body(BodyInserters.fromFormData(erpRequest.getFormData()))
//				.headers(newRequestHeader -> {
//					if (erpRequest.getHeaders() != null) {
//						newRequestHeader.addAll(erpRequest.getHeaders());
//					}
//				})
//				.retrieve();
//
//			responseEntity = respenseSpec.toEntity(String.class).block();
//			
//			return new RestResponse<>(responseEntity);
//		} catch (RestClientResponseException e) {
//			exception = e;
//			return new RestResponse<>(e);
//		} catch (WebClientResponseException e) {
//			exception = e;
//			return new RestResponse<>(e);
//		} catch (Exception e) {
//			exception = e;
//			return new RestResponse<>(e);
//		} finally {
//			// 응답로그등록
//			latencyTimes = System.currentTimeMillis() - start;
//			logging(latencyTimes, orgUrl, method, erpRequest.getHeaders(), erpRequest.getFormData(), responseEntity, exception);
//		}
//	}
//
//	private WebClient getWebclient(int connectionTimeoutSeconds, int responseTimeoutSeconds) {
//		HttpClient client = HttpClient.create();
//		if (connectionTimeoutSeconds > 0) {
//			client.responseTimeout(Duration.ofSeconds(responseTimeoutSeconds))
//				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeoutSeconds*1000); // Connection Timeout
//		}
//		ClientHttpConnector connector = new ReactorClientHttpConnector(client);
//		return WebClient.builder().clientConnector(connector).build();
//	}
//
//	private <T> void logging(long latencyTimes, URI url, HttpMethod method, MultiValueMap<String, String> reqHeaders, MultiValueMap<String, String> formData, ResponseEntity<T> responseEntity, Exception e) {
//		if (Objects.isNull(e)) {
//			Object resBody = responseEntity.getBody();
//			logging(latencyTimes, url, method, reqHeaders==null?null:JsonUtils.string(reqHeaders), formData==null?null:JsonUtils.string(formData), responseEntity.getStatusCodeValue(),
//					responseEntity.getStatusCode().getReasonPhrase(), responseEntity.getHeaders(),
//					resBody==null?null:JsonUtils.string(resBody), null);
//		}
//		else if (e instanceof RestClientResponseException) {
//			RestClientResponseException re = (RestClientResponseException) e;
//			logging(latencyTimes, url, method, reqHeaders==null?null:JsonUtils.string(reqHeaders), formData==null?null:JsonUtils.string(formData), re.getRawStatusCode(), re.getStatusText(),
//					re.getResponseHeaders(), re.getResponseBodyAsString(), e);
//		} else {
//			logging(latencyTimes, url, method, reqHeaders==null?null:JsonUtils.string(reqHeaders), formData==null?null:JsonUtils.string(formData), -1, null, null, null, e);
//		}
//	}
//
//	private void logging(long latencyTimes, URI url, HttpMethod method, String reqHeaders, String formData,
//			int statusCode,String statusText, HttpHeaders resHeaders, String resBody, Exception e) {
//		StringBuilder sb = new StringBuilder().append('\n');
//		sb.append("##################################################################").append('\n');
//		sb.append("# [REST_API] latency-time: ").append(latencyTimes).append(" ms\n");
//		sb.append("##[Request]#######################################################").append('\n');
//		sb.append("# URL    : ").append(url).append('\n');
//		sb.append("# Method : ").append(method).append('\n');
//		sb.append("# Headers: ").append(reqHeaders).append('\n');
//		sb.append("# FormData   : ").append(formData).append('\n');
//		sb.append("##[Response]######################################################").append('\n');
//		sb.append("# Code   : ").append(statusCode).append(' ').append(statusText).append('\n');
//		sb.append("# Headers: ").append(resHeaders).append('\n');
//		sb.append("# Body   : ").append(resBody).append('\n');
//		sb.append("##################################################################");
//
//		if (Objects.isNull(e)) {
//			if (log.isDebugEnabled()) {
//				log.debug(sb.toString());
//			}
//		} else {
//			sb.append('\n').append("# Exception : ");
//			log.error(sb.toString(), e);
//		}
//	}

	private StInfLog registerInfReqLog(ErpRequest request, HttpMethod requestMethod) {
		// 요청로그
		StInfLog stInfLog = new StInfLog();
		try {
			stInfLog.setInfTypCd(request.getIfType()); // 요청로그
			stInfLog.setReqRplyGbCd("1"); // 요청로그
			stInfLog.setInfId(request.getIfId());
			stInfLog.setReqUrl(request.getUrl());
			stInfLog.setReqMethod(requestMethod.name());
			stInfLog.setReqHerVal(JsonUtils.string(request.getHeaders()));
			stInfLog.setQryStrPrmt(JsonUtils.string(request.getFormData()));
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

	private StInfLog registerInfResLog(StInfLog stInfLog, String statusCdode, MultiValueMap<String,String> headers, String body) {
		try {
			stInfLog.setReqRplyGbCd("2"); // 응답로그
			stInfLog.setUprInfLogSeq(stInfLog.getInfLogSeq());
			stInfLog.setInfLogSeq(null);
			stInfLog.setRplyStatCd(StringUtils.substring(statusCdode, 0, 3));
			stInfLog.setRplyHerVal(JsonUtils.string(headers));
			stInfLog.setRplyCont(body);
			infLogService.registerInfLogService(stInfLog);
		} catch (Exception e) {
			// 로그 작성 중 exception은 무시하도록 처리한다.
			log.error(e.getMessage(), e);
		}
		
		return stInfLog;
	}

}
