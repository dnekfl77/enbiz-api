package com.x2bee.api.common.app.service.payment.impl;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.x2bee.api.common.app.dto.request.payment.TossReadyRequest;
import com.x2bee.api.common.app.dto.response.payment.TossReadyResponse;
import com.x2bee.api.common.app.service.payment.TossPayService;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
	@Value("${payment.toss.paymentsUrl:}")
	private URI paymentsUri;

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
}
