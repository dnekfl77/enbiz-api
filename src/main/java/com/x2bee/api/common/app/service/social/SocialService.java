package com.x2bee.api.common.app.service.social;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.x2bee.api.common.app.entity.SocialRequest;
import com.x2bee.api.common.app.enums.GenderType;
import com.x2bee.api.common.app.enums.SocialType;
import com.x2bee.common.base.util.PhoneNoUtils.PhoneNo;
import com.x2bee.common.base.util.StringFormatter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

public interface SocialService {

	SocialType getSocialType();
	
	Token getToken(SocialRequest socialRequest);
	
	SocialUser me(Token token);
	
	public static SocialService newInstance(SocialType socialType) {
		var servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		var context = WebApplicationContextUtils.getWebApplicationContext(servletRequest.getServletContext());
		
		switch (socialType) {
		case NAVER:
			return context.getBean(SocialNaverService.class);
		case KAKAO:
			return context.getBean(SocialKakaoService.class);
		default:
			throw new IllegalArgumentException(StringFormatter.format("Not supported type: {}", socialType));
		}
	}
	
	@Getter
	@Setter
	@Accessors(chain = true)
	@ToString
	public static class Token {
		@JsonProperty("access_token")
		private String accessToken;
	}
	
	@Getter
	@Setter
	@Accessors(chain = true)
	@ToString
	public static class SocialUser {
		private String id;
		private String name;
		private String email;
		private String ci;
		private String birthday;
		private PhoneNo mobileNo;
		private GenderType genderType;
		private SocialType socialType;
		private Boolean agreeTerms;
	}
}