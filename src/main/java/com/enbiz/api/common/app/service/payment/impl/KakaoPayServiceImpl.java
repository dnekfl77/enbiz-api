package com.enbiz.api.common.app.service.payment.impl;

import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.enbiz.api.common.app.service.payment.KakaoPayService;
import com.enbiz.common.base.util.StringFormatter;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

/**
 * 카카오페이 서비스
 *
 * 
 * 
 */
@Slf4j
@Service
@Lazy
public class KakaoPayServiceImpl implements KakaoPayService {

	private static HttpClient httpClient = HttpClient.create()
			.wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50000)
			.responseTimeout(Duration.ofMillis(50000))
			.doOnConnected(c -> c.addHandlerLast(new ReadTimeoutHandler(50000, TimeUnit.MILLISECONDS))
					.addHandlerLast(new WriteTimeoutHandler(50000, TimeUnit.MILLISECONDS)));

	@Value("${payment.kakao.adminKey:}")
	private String adminKey;
	@Value("${payment.kakao.cid:}")
	private String cid;
	@Value("${payment.kakao.readyUrl:}")
	private String readyUrl;
	@Value("${payment.kakao.approveUrl:}")
	private String approveUrl;
	@Value("${payment.kakao.cancelUrl:}")
	private String cancelUrl;

	@Override
	public Map<String, Object> ready(String partnerOrderId, String partnerUserId, String itemName, Long quantity, Long totalAmount, String approvalUrl,
			String cancelUrl, String failUrl) {
		var webClient = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();
		
		var response = webClient.post()
				.uri(URI.create(readyUrl))
				.header("Authorization", StringFormatter.format("KakaoAK {}", adminKey))
				.body(BodyInserters.fromFormData("cid", cid)
						.with("partner_order_id", partnerOrderId)
						.with("partner_user_id", partnerUserId)
						.with("item_name", itemName)
						.with("quantity", String.valueOf(quantity))
						.with("total_amount", String.valueOf(totalAmount))
						.with("tax_free_amount", "0")
						.with("approval_url", approvalUrl)
						.with("cancel_url", cancelUrl)
						.with("fail_url", failUrl))
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
				;

		log.debug("response: {}", response);
		
		return response.block();
	}

	@Override
	public Map<String, Object> approve(Map<String, Object> request) {
		var webClient = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();
		
		var response = webClient.post()
				.uri(URI.create(approveUrl))
				.header("Authorization", StringFormatter.format("KakaoAK {}", adminKey))
				.body(BodyInserters.fromFormData("cid", cid)
						.with("tid", MapUtils.getString(request, "tid"))
						.with("partner_order_id", MapUtils.getString(request, "partnerOrderId"))
						.with("partner_user_id", MapUtils.getString(request, "partnerUserId"))
						.with("pg_token", MapUtils.getString(request, "pgToken")))
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
				;

		log.debug("response: {}", response);
		
		return response.block();
	}

	@Override
	public Map<String, Object> cancel(Map<String, Object> request) {
		var webClient = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();
		
		var response = webClient.post()
				.uri(URI.create(cancelUrl))
				.header("Authorization", StringFormatter.format("KakaoAK {}", adminKey))
				.body(BodyInserters.fromFormData("cid", cid)
						.with("tid", MapUtils.getString(request, "tid"))
						.with("cancel_amount", MapUtils.getString(request, "cancelAmount"))
						.with("cancel_tax_free_amount", "0"))
//				.bodyToMono(KakaoReturnResponse.class)
				.exchangeToMono(clientResponse -> {
					if (clientResponse.statusCode().is4xxClientError()) {
						clientResponse.body((c, context) -> {
							return c.getBody();
						});
					}
					return clientResponse.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
				})
				;

		log.debug("response: {}", response);
		
		return response.block();
	}

}
