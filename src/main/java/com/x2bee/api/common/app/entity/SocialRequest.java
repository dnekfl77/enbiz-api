package com.x2bee.api.common.app.entity;

import javax.validation.constraints.NotNull;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Alias("SocialRequest")
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SocialRequest {
	private String code; // Social Code
	private String state; // 유효성 검사
	private String redirectUrl;
	@NotNull
	private String socialType;
}
