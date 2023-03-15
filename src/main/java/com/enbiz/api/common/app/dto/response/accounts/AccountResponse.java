package com.enbiz.api.common.app.dto.response.accounts;

import com.enbiz.api.common.app.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AccountResponse {

	private Long id;
	private String username;
	private String email;

	public static AccountResponse from(Account account) {
		return AccountResponse.builder()
				.id(account.getId())
				.username(account.getUsername())
				.email(account.getEmail())
				.build();
	}
}
