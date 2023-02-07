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

import com.x2bee.api.common.app.service.payment.NaverPayService;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Service
@Lazy
@Slf4j
public class NaverPayServiceImpl implements NaverPayService {

	private static HttpClient httpClient = HttpClient.create()
			.wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50000)
			.responseTimeout(Duration.ofMillis(50000))
			.doOnConnected(c -> c.addHandlerLast(new ReadTimeoutHandler(50000, TimeUnit.MILLISECONDS))
					.addHandlerLast(new WriteTimeoutHandler(50000, TimeUnit.MILLISECONDS)));
	
	@Value("${payment.naver.paymentUrl:}")
	private String paymentUri;
	@Value("${payment.naver.cancelUrl:}")
	private String cancelUrl;
	@Value("${payment.naver.confirmUrl:}")
	private String confirmUrl;
	@Value("${payment.naver.clientId:}")
	private String clientId;
	@Value("${payment.naver.clientSecret:}")
	private String clientSecret;

	@Override
	public Map<String, Object> payment(String paymentId) {
		var webClient = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();

		var response = webClient.post()
				.uri(paymentUri)
				.header("X-Naver-Client-Id", clientId)
				.header("X-Naver-Client-Secret", clientSecret)
				.body(BodyInserters.fromFormData("paymentId", paymentId))
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
				;

		log.debug("response: {}", response);

		return response.block();
	}

	@Override
	public Map<String, Object> cancel(String paymentId, long cancelAmount, String reason) {
		return null;
	}

	@Override
	public Map<String, Object> purchaseConfirm(String paymentId, String clientId, String clientSecret) {
		return null;
	}
}
