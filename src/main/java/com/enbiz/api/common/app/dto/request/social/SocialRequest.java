package com.enbiz.api.common.app.dto.request.social;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SocialRequest {
	private String code; // Social Code
	private String state; // 유효성 검사
	private String redirectUrl;
	@NotNull
	private String socialType;
}
