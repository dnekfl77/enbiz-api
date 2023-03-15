package com.enbiz.api.common.app.controller.accounts;

import java.util.Objects;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enbiz.api.common.app.dto.request.accounts.RefreshRequest;
import com.enbiz.api.common.app.dto.request.social.SocialRequest;
import com.enbiz.api.common.app.dto.response.accounts.AccountResponse;
import com.enbiz.api.common.app.dto.response.accounts.TokenResponse;
import com.enbiz.api.common.app.dto.response.social.SocialResponse;
import com.enbiz.api.common.app.service.account.AccountService;
import com.enbiz.common.base.rest.Response;
import com.enbiz.common.base.token.UserDetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/accounts")
@Lazy
@Slf4j
@RequiredArgsConstructor
public class AccountController {
	
	private final AccountService accountService;

	@PostMapping(path = "/signSocial")
	public Response<SocialResponse> signSocial(@RequestBody SocialRequest request) {
		log.debug("request: {}", request);
		
		return Response.success(accountService.signSocial(request));
	}
	
	@GetMapping(path = "/info")
	public Response<AccountResponse> info(@AuthenticationPrincipal UserDetail userDetail) {
		log.debug("userDetail: {}", userDetail);
		
		if (Objects.isNull(userDetail)) {
			return Response.failure("Not found");
		}
		
		return Response.success(accountService.getAccount(userDetail.getUsername())); 
	}
	
	@PostMapping(path = "/refresh")
	public Response<TokenResponse> refresh(@RequestBody RefreshRequest request) {
		log.debug("request: {}", request);
		
		return Response.success(accountService.refreshToken(request));
	}
}
