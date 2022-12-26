package com.x2bee.api.common.app.service.interfacecommon;

import java.net.URI;
import java.time.Duration;
import java.util.Objects;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import com.x2bee.common.base.rest.RestResponse;
import com.x2bee.common.base.rest.infapi.InfRequest;
import com.x2bee.common.base.util.JsonUtils;

import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;

@Component
@Slf4j
public class InfRestApi {
	public <T> RestResponse<T> execute(InfRequest infRequest, HttpMethod method, Object type) {
		long latencyTimes = -1;

		ResponseEntity<T> responseEntity = null;
		Exception exception = null;
		URI orgUrl = null;
		long start = System.currentTimeMillis();
		try {
			WebClient webClient = getWebclient(infRequest.getConnectionTimeoutSeconds(), infRequest.getResponseTimeoutSeconds()); // TODO 
	
			// 한글 파라미터 로깅을 위하여.
			UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(infRequest.getUrl());
			orgUrl = uriComponentsBuilder.build().toUri(); // 한글 파라미터 로깅을 위하여.
			uriComponentsBuilder.encode();
			URI url = uriComponentsBuilder.build().toUri();

			ResponseSpec respenseSpec;
			// request body 없는 경우
			if (infRequest.getRequestObject() == null && infRequest.getFormData() == null) { // method == HttpMethod.GET || method == HttpMethod.DELETE
				respenseSpec = webClient
						.method(method)
						.uri(url)
						.headers(newRequestHeader -> {
							if (infRequest.getHeaders() != null) {
								newRequestHeader.addAll(infRequest.getHeaders());
							}
						})
						.retrieve();
			}
			// formdata 있는 경우
			else if (infRequest.getFormData() != null) {
				respenseSpec = webClient
						.method(method)
						.uri(url)
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.body(BodyInserters.fromFormData(infRequest.getFormData()))
						.headers(newRequestHeader -> {
							if (infRequest.getHeaders() != null) {
								newRequestHeader.addAll(infRequest.getHeaders());
							}
						})
						.retrieve();				
			}
			// request body 있는 경우
			else { // infRequest.getRequestObject() != null
				respenseSpec = webClient
						.method(method)
						.uri(url)
						.body(BodyInserters.fromValue(infRequest.getRequestObject()))
						.headers(newRequestHeader -> {
							if (infRequest.getHeaders() != null) {
								newRequestHeader.addAll(infRequest.getHeaders());
							}
						})
						.retrieve();				
			}

			// 결과 type이 Class<T>로 지정된 경우
			if (type instanceof Class<?>) {
				responseEntity = respenseSpec.toEntity((Class<T>)type).block();
			}
			// 결과 type이 ParameterizedTypeReference<T>로 지정된 경우
			else {
				responseEntity = respenseSpec.toEntity((ParameterizedTypeReference<T>)type).block();
			}
			
			return new RestResponse<>(responseEntity);
		} catch (RestClientResponseException e) {
			exception = e;
			return new RestResponse<>(e);
		} catch (WebClientResponseException e) {
			exception = e;
			return new RestResponse<>(e);
		} catch (Exception e) {
			exception = e;
			return new RestResponse<>(e);
		} finally {
			// 응답로그등록
			latencyTimes = System.currentTimeMillis() - start;
			logging(latencyTimes, orgUrl, method, infRequest.getHeaders(), infRequest.getFormData(), responseEntity, exception);
		}
	}

	private WebClient getWebclient(int connectionTimeoutSeconds, int responseTimeoutSeconds) {
		HttpClient client = HttpClient.create();
		if (connectionTimeoutSeconds > 0) {
			client.responseTimeout(Duration.ofSeconds(responseTimeoutSeconds))
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeoutSeconds*1000); // Connection Timeout
		}
		ClientHttpConnector connector = new ReactorClientHttpConnector(client);
		return WebClient.builder().clientConnector(connector).build();
	}

	private <T> void logging(long latencyTimes, URI url, HttpMethod method, MultiValueMap<String, String> reqHeaders, MultiValueMap<String, String> formData, ResponseEntity<T> responseEntity, Exception e) {
		if (Objects.isNull(e)) {
			Object resBody = responseEntity.getBody();
			logging(latencyTimes, url, method, reqHeaders==null?null:JsonUtils.string(reqHeaders), formData==null?null:JsonUtils.string(formData), responseEntity.getStatusCodeValue(),
					responseEntity.getStatusCode().getReasonPhrase(), responseEntity.getHeaders(),
					resBody==null?null:JsonUtils.string(resBody), null);
		}
		else if (e instanceof RestClientResponseException) {
			RestClientResponseException re = (RestClientResponseException) e;
			logging(latencyTimes, url, method, reqHeaders==null?null:JsonUtils.string(reqHeaders), formData==null?null:JsonUtils.string(formData), re.getRawStatusCode(), re.getStatusText(),
					re.getResponseHeaders(), re.getResponseBodyAsString(), e);
		} else {
			logging(latencyTimes, url, method, reqHeaders==null?null:JsonUtils.string(reqHeaders), formData==null?null:JsonUtils.string(formData), -1, null, null, null, e);
		}
	}

	private void logging(long latencyTimes, URI url, HttpMethod method, String reqHeaders, String formData,
			int statusCode,String statusText, HttpHeaders resHeaders, String resBody, Exception e) {
		StringBuilder sb = new StringBuilder().append('\n');
		sb.append("##################################################################").append('\n');
		sb.append("# [REST_API] latency-time: ").append(latencyTimes).append(" ms\n");
		sb.append("##[Request]#######################################################").append('\n');
		sb.append("# URL    : ").append(url).append('\n');
		sb.append("# Method : ").append(method).append('\n');
		sb.append("# Headers: ").append(reqHeaders).append('\n');
		sb.append("# FormData   : ").append(formData).append('\n');
		sb.append("##[Response]######################################################").append('\n');
		sb.append("# Code   : ").append(statusCode).append(' ').append(statusText).append('\n');
		sb.append("# Headers: ").append(resHeaders).append('\n');
		sb.append("# Body   : ").append(resBody).append('\n');
		sb.append("##################################################################");

		if (Objects.isNull(e)) {
			if (log.isDebugEnabled()) {
				log.debug(sb.toString());
			}
		} else {
			sb.append('\n').append("# Exception : ");
			log.error(sb.toString(), e);
		}
	}


}
