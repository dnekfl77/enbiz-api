package com.enbiz.api.common.app.service.social;

import com.enbiz.api.common.app.dto.request.social.SocialRequest;
import com.enbiz.api.common.app.enums.GenderType;
import com.enbiz.api.common.app.enums.SocialType;
import com.enbiz.common.base.context.ApplicationContextWrapper;
import com.enbiz.common.base.util.PhoneNoUtils.PhoneNo;
import com.enbiz.common.base.util.StringFormatter;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

public interface SocialService {

	SocialType getSocialType();
	
	Token getToken(SocialRequest socialRequest);
	
	SocialUser me(Token token);
	
	public static SocialService newInstance(SocialType socialType) {
		var context = ApplicationContextWrapper.getApplicationContext();
		
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