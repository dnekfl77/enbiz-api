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
import com.google.common.base.Joiner;
import com.x2bee.api.common.app.dto.request.accounts.SocialRequest;
import com.x2bee.api.common.app.enums.GenderType;
import com.x2bee.api.common.app.enums.SocialType;
import com.x2bee.api.common.app.service.social.SocialKakaoService;
import com.x2bee.common.base.util.PhoneNoUtils;
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
public class SocialKakaoServiceImpl implements SocialKakaoService {

	static HttpClient httpClient = HttpClient.create()
			.wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
	
	@Value("${social.cert.kakao.client_id:}")
	private String clientId;
	
	final Environment environment;
	
	@Override
	public SocialType getSocialType() {
		return SocialType.KAKAO;
	}
	
	@Override
	public Token getToken(SocialRequest socialRequest) {
		var webClient = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.build();

		var token = webClient.post()
				.uri("https://kauth.kakao.com/oauth/token")
				.body(fromFormData("client_id", clientId)
						.with("grant_type", "authorization_code")
						.with("redirect_uri", socialRequest.getRedirectUrl())
						.with("code", socialRequest.getCode()))
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

		var result = webClient.get()
				.uri("https://kapi.kakao.com/v2/user/me")
				.header("Authorization", StringFormatter.format("Bearer {}", token.getAccessToken()))
				.retrieve()
				.bodyToMono(Me.class)
				.block();

		log.debug("result: {}", result);
		
		return new SocialUser()
				.setEmail(result.account.email)
				.setId(result.id)
				.setName(StringUtils.firstNonBlank(result.account.legalName, 
						StringUtils.substringBefore(result.account.profile.nickname, " /")))
				.setBirthday(Joiner.on("-").skipNulls().join(result.account.birthyear, result.account.birthday))
				.setMobileNo(PhoneNoUtils.valueOf(StringUtils.replace(result.account.phoneNumber, "+82 ", "0"))) // 
				.setGenderType(GenderType.of(result.account.gender))
				// TODO 임시 CI - 추후 변경
				.setCi(StringUtils.defaultString(result.account.ci, defaultCi()))
				.setSocialType(SocialType.KAKAO);
	}
		
	private String defaultCi() {
		return !environment.acceptsProfiles(Profiles.of("ci", "dev")) ? ""
				: "VUCnsIA1XrPrqbjFvWiwZRs+o9mq/nKd2eGWX3yFLb7OMq7MSh9ueeWxSdxGBHUC6cBolgEi6ORHXrVvRsNLHQ==";
	}
	
	@Getter
	@Setter
	@ToString
	static class Me {
		private String id;
		@JsonProperty("kakao_account")
		private Account account;
	}
	
	@Getter
	@Setter
	@ToString
	static class Account {
		private Profile profile;
		private String email;
		private String ci;
		@JsonProperty("phone_number")
		private String phoneNumber;
		private String birthyear;
		private String birthday;
		@JsonProperty("birthday_type")
		private String birthdayType;
		private String gender;
		@JsonProperty("legal_name")
		private String legalName;
	}
	
	@Getter
	@Setter
	@ToString
	static class Profile {
		private String nickname;
	}
}
