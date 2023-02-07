package com.x2bee.api.common.app.service.payment.impl;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.common.collect.ImmutableMap;
import com.inicis.std.util.SignatureUtil;
import com.x2bee.api.common.app.service.payment.InicisPayService;
import com.x2bee.api.common.base.utils.ShaCryptUtils;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Service
@Lazy
@Slf4j
public class InicisPayServiceImpl implements InicisPayService {
	
	private static String key = "ItEQKi3rY7uvDS8l";
	
	@Value("${payment.inicis.cancelUrl:}")
	private String inicisCancelUrl;

	private static HttpClient httpClient = HttpClient.create()
			.wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50000)
			.responseTimeout(Duration.ofMillis(50000))
			.doOnConnected(c -> c.addHandlerLast(new ReadTimeoutHandler(50000, TimeUnit.MILLISECONDS))
					.addHandlerLast(new WriteTimeoutHandler(50000, TimeUnit.MILLISECONDS)));
	
	@Override
	public Map<String, Object> payment(String authUrl, String netCancelUrl, String mid, String authToken, String timestamp) {
		log.debug("authUrl: {}", authUrl);
		
		var webClient = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();

		try {
			var response = webClient.post()
					.uri(authUrl)
					.body(BodyInserters.fromFormData("mid", mid)
							.with("authToken", authToken)
							.with("timestamp", timestamp)
							.with("signature", generateSignature(authToken, timestamp))
							.with("charset", "UTF-8")
							.with("format", "JSON"))
					.retrieve()
					.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
					;

			return response.block();		
		} finally {
			// netCancel(netCancelUrl, mid, authToken, timestamp);	
		}
	}

	@Override
	public Map<String, Object> cancel(String payMethod, String type, String timestamp, String clientIp, String mid, String tid, String msg) {
		var webClient = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();
// key + type + paymethod + timestamp + clientIp + mid + tid;
		var response = webClient.post()
				.uri(inicisCancelUrl)
				.body(BodyInserters.fromFormData("type", type)
						.with("paymethod", payMethod)
						.with("timestamp", timestamp)
						.with("clientIp", clientIp)
						.with("mid", mid/*"INIpayTest"*/)
						.with("tid", tid)
						.with("msg", "취소요청")
						.with("hashData", ShaCryptUtils.encryptSha512(key, type, payMethod, timestamp, clientIp, mid, tid)))
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
				;

		return response.block();
	}
	
	@Override
	public Map<String, Object> netCancel(String netCancelUrl, String mid, String authToken, String timestamp) {
		var webClient = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();
		
		var response = webClient.post()
				.uri(netCancelUrl)
				.body(BodyInserters.fromFormData("mid", mid)
						.with("authToken", authToken)
						.with("timestamp", timestamp)
						.with("signature", generateSignature(authToken, timestamp))
						.with("charset", "UTF-8")
						.with("format", "JSON"))
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
				;

		return response.block();
	}

	private String generateSignature(String authToken, String timestamp) {
		try {
			return SignatureUtil.makeSignature(
					ImmutableMap.<String, String>builder().put("authToken", authToken).put("timestamp", timestamp).build());
		} catch (Exception e) {
			return StringUtils.EMPTY;
		}
	}
}
