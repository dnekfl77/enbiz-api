package com.enbiz.api.common.app.service.account;

import com.enbiz.api.common.app.dto.request.accounts.RefreshRequest;
import com.enbiz.api.common.app.dto.request.social.SocialRequest;
import com.enbiz.api.common.app.dto.response.accounts.AccountResponse;
import com.enbiz.api.common.app.dto.response.accounts.TokenResponse;
import com.enbiz.api.common.app.dto.response.social.SocialResponse;

public interface AccountService {

	SocialResponse signSocial(SocialRequest request);

	AccountResponse getAccount(String username);

	TokenResponse refreshToken(RefreshRequest request);
}
