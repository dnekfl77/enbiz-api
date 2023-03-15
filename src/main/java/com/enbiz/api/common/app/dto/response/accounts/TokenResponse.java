package com.enbiz.api.common.app.dto.response.accounts;

import com.enbiz.api.common.app.entity.Account;
import com.enbiz.common.base.token.MemberTokenDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TokenResponse {

	private String username;
	private String name;
	private String email;
	private String accessToken;
	private String refreshToken;

	public static TokenResponse from(Account account, MemberTokenDto memberToken) {
		return TokenResponse.builder()
				.username(account.getUsername())
				.name(account.getName())
				.email(account.getEmail())
				.accessToken(memberToken.getAccessToken())
				.refreshToken(memberToken.getRefreshToken())
				.build();
	}
}
