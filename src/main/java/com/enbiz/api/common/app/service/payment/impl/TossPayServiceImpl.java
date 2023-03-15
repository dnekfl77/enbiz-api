package com.enbiz.api.common.app.service.payment.impl;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.enbiz.api.common.app.dto.request.payment.TossApproveRequest;
import com.enbiz.api.common.app.dto.request.payment.TossReadyRequest;
import com.enbiz.api.common.app.dto.response.payment.TossApproveResponse;
import com.enbiz.api.common.app.dto.response.payment.TossReadyResponse;
import com.enbiz.api.common.app.service.payment.TossPayService;
import com.enbiz.common.base.util.StringFormatter;
import com.google.common.base.Joiner;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Service
@RequiredArgsConstructor
@Slf4j
public class TossPayServiceImpl implements TossPayService {

	/*
		{
		  "orderNo": "1",
		  "amount": 25000,
		  "amountTaxFree": 0,
		  "productDesc": "토스티셔츠",
		  "apiKey": "sk_test_w5lNQylNqa5lNQe013Nq",
		  "autoExecute": true,
		  "resultCallback": "https://YOUR-SITE.COM/callback",
		  "retUrl": "http://YOUR-SITE.COM/ORDER-CHECK",
		  "retCancelUrl": "http://YOUR-SITE.COM/close"
		}
	 */
	
	private static HttpClient httpClient = HttpClient.create()
			.wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50000)
			.responseTimeout(Duration.ofMillis(50000))
			.doOnConnected(c -> c.addHandlerLast(new ReadTimeoutHandler(50000, TimeUnit.MILLISECONDS))
					.addHandlerLast(new WriteTimeoutHandler(50000, TimeUnit.MILLISECONDS)));

	@Value("${payment.toss.apiKey:}")
	private String apiKey;
	@Value("${payment.toss.secretKey:}")
	private String secretKey;
	@Value("${payment.toss.paymentsUrl:}")
	private URI paymentsUri;
	@Value("${payment.toss.approveUrl:}")
	private URI approveUrl;

	@Override
	public TossReadyResponse ready(TossReadyRequest request) {
		log.debug("request: {}", request);
		
		var webClient = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();

		var response = webClient.post()
				.uri(paymentsUri)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(request.toBuilder().apiKey(apiKey).build()))
				.retrieve()
				.bodyToMono(TossReadyResponse.class)
				;

		return response.block();
	}
	
	@Override
	public TossApproveResponse approve(TossApproveRequest request) {
		log.debug("request: {}", request);
		
		
		var webClient = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();

		var response = webClient.post()
				.uri(approveUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", StringFormatter.format("Basic {}", encodeSecretKey(secretKey)))
				.body(BodyInserters.fromValue(request))
				.retrieve()
				.onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.empty())
				.bodyToMono(TossApproveResponse.class)
				.onErrorResume(WebClientResponseException.class, e -> {
					return Mono.error(e);
				})
				.log()
				;

		return response.block();
	}
	
	String encodeSecretKey(String secretKey) {
		return Base64.encodeBase64String(Joiner.on("").join(secretKey, ":").getBytes());
	}
}
