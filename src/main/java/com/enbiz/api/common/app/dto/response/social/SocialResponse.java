package com.enbiz.api.common.app.dto.response.social;

import com.enbiz.api.common.app.entity.Account;
import com.enbiz.common.base.token.MemberTokenDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SocialResponse {
	
	private String username;
	private String name;
	private String email;
	private String accessToken;
	private String refreshToken;
	
	public static SocialResponse from(Account account, MemberTokenDto memberToken) {
		return SocialResponse.builder()
				.username(account.getUsername())
				.name(account.getName())
				.email(account.getEmail())
				.accessToken(memberToken.getAccessToken())
				.refreshToken(memberToken.getRefreshToken())
				.build();
	}
}
