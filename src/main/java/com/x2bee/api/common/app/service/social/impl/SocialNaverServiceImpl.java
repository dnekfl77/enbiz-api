package com.x2bee.api.common.app.service.social.impl;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.x2bee.api.common.app.dto.request.accounts.SocialRequest;
import com.x2bee.api.common.app.enums.SocialType;
import com.x2bee.api.common.app.service.social.SocialNaverService;
import com.x2bee.common.base.util.StringFormatter;

import io.netty.handler.logging.LogLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;


@Service
@Lazy
@Slf4j
@RequiredArgsConstructor
public class SocialNaverServiceImpl implements SocialNaverService {

	static HttpClient httpClient = HttpClient.create()
			.wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
	
	@Value("${social.cert.naver.client_id:}")
	private String clientId;
	@Value("${social.cert.naver.client_secret:}")
	private String clientSecret;
	
	final Environment environment;
	
	@Override
	public Token getToken(SocialRequest socialRequest) {
		var webClient = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.build();

		var token = webClient.post()
				.uri("https://nid.naver.com/oauth2.0/token")
				.body(fromFormData("client_id", clientId)
						.with("client_secret", clientSecret)
						.with("grant_type", "authorization_code")
						.with("code", socialRequest.getCode())
						.with("state", socialRequest.getState()))
				.retrieve()
				.bodyToMono(Token.class)
				.block();
		
		log.debug("token: {}", token);
		
		return token;
	}

	@Override
	public SocialUser me(Token token) {
		var webClient = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.build();

		var result = webClient.get().uri("https://openapi.naver.com/v1/nid/me")
				.header("Authorization", StringFormatter.format("Bearer {}", token.getAccessToken()))
				.retrieve()
				.bodyToMono(Me.class)
				.block();

		log.debug("result: {}", result);
		
		return new SocialUser()
				.setEmail(result.response.email)
				.setId(result.response.id)
				.setName(result.response.name)
				// TODO 임시 CI - 추후 변경
				.setCi(StringUtils.defaultString(result.response.ci, defaultCi()))
				.setSocialType(SocialType.NAVER);
	}
	
	@Override
	public SocialType getSocialType() {
		return SocialType.NAVER;
	}
	
	private String defaultCi() {
		return !environment.acceptsProfiles(Profiles.of("ci", "dev")) ? ""
				: "+ZtjyMProvZLw5JTpanjWQmXGjlXEnZdjQZSwihySY3k+ADqYV+gFDoU0nQ9VIunOb+wGxXA7T4PukVaHW0WMA==";
	}

	@Getter
	@Setter
	@ToString
	static class Me {
		@JsonProperty("resultcode")
		private String resultCode;
		private String message;
		private Response response;
	}
	
	@Getter
	@Setter
	@ToString
	static class Response {
		private String ci;
		private String id;
		private String email;
		private String name;
		private String birthday; // yyyy-MM-dd
	}
}
