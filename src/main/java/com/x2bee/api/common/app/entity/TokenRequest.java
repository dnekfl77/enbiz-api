package com.x2bee.api.common.app.entity;

import javax.validation.constraints.NotEmpty;

import org.apache.ibatis.type.Alias;

import com.x2bee.common.base.entity.BaseFoEntity;

import lombok.Getter;
import lombok.Setter;

@Alias("tokenRequest")
@Getter
@Setter
public class TokenRequest extends BaseFoEntity {
	@NotEmpty
	private String loginId;
	//@NotEmpty
	private String password;
}
