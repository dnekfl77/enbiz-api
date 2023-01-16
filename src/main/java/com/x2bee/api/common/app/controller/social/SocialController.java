package com.x2bee.api.common.app.controller.social;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.x2bee.api.common.app.dto.request.accounts.SocialRequest;
import com.x2bee.api.common.app.enums.SocialType;
import com.x2bee.api.common.app.service.social.SocialService;
import com.x2bee.api.common.app.service.social.SocialService.SocialUser;
import com.x2bee.common.base.rest.Response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/social")
@Lazy
@Slf4j
@RequiredArgsConstructor
public class SocialController {


	@PostMapping("/sign")
	public Response<SocialUser> sign(@RequestBody SocialRequest request) {
		log.debug("request: {}", request);
		
		SocialService socialService = SocialService.newInstance(SocialType.ofType(request.getSocialType()));

		return new Response<SocialUser>().setPayload(socialService.me(socialService.getToken(request)));
	}
}
