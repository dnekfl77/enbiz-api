package com.enbiz.api.common.app.service.account.impl;

import java.util.Objects;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.enbiz.api.common.app.dto.request.accounts.RefreshRequest;
import com.enbiz.api.common.app.dto.request.social.SocialRequest;
import com.enbiz.api.common.app.dto.response.accounts.AccountResponse;
import com.enbiz.api.common.app.dto.response.accounts.TokenResponse;
import com.enbiz.api.common.app.dto.response.social.SocialResponse;
import com.enbiz.api.common.app.entity.Account;
import com.enbiz.api.common.app.entity.criteria.AccountCriteria;
import com.enbiz.api.common.app.enums.SocialType;
import com.enbiz.api.common.app.repository.main.account.AccountTrxMapper;
import com.enbiz.api.common.app.repository.replica.account.AccountMapper;
import com.enbiz.api.common.app.service.account.AccountService;
import com.enbiz.api.common.app.service.social.SocialService;
import com.enbiz.api.common.app.service.social.SocialService.SocialUser;
import com.enbiz.common.base.token.MemberTokenService;
import com.enbiz.common.base.token.UserDetail;

import lombok.RequiredArgsConstructor;

@Lazy
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	private final MemberTokenService memberTokenService;
	private final AccountTrxMapper accountTrxMapper;
	private final AccountMapper accountMapper;

	@Override
	public SocialResponse signSocial(SocialRequest request) {
		var socialUser= getSocialUser(request);
		var account = saveAndGetAccount(socialUser);
		var memberToken = memberTokenService.create(UserDetail.builder().username(account.getUsername()).id(account.getId()).build());
	
		return SocialResponse.from(account, memberToken);
	}
	
	@Override
	public AccountResponse getAccount(String username) {
		return AccountResponse.from(accountMapper.findByCriteria(AccountCriteria.builder().username(username).build()));
	}

	@Override
	public TokenResponse refreshToken(RefreshRequest request) {
		var jwtToken = memberTokenService.parseRefreshToken(request.getRefreshToken());
		var username = (String) jwtToken.getBody().get("username");
		var account = accountMapper.findByCriteria(AccountCriteria.builder().username(username).build());
		var memberToken = memberTokenService.create(UserDetail.builder().username(account.getUsername()).id(account.getId()).build());
		
		return TokenResponse.from(account, memberToken);
	}
	
	private SocialUser getSocialUser(SocialRequest request) {
		SocialService socialService = SocialService.newInstance(SocialType.ofType(request.getSocialType()));
		
		return socialService.me(socialService.getToken(request));
	}

	private Account saveAndGetAccount(SocialUser socialUser) {
		var account = Account.from(socialUser);
		var saved = accountMapper.findByCriteria(AccountCriteria.builder().socialToken(socialUser.getId()).build());
		if (Objects.isNull(saved)) {
			accountTrxMapper.save(account);
		}
		return account;
	}
}
